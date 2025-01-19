package io.faroxy.ui;

import io.faroxy.FaroxyServerApplication;
import io.faroxy.model.RequestLog;
import io.faroxy.model.ResponseLog;
import io.faroxy.service.FaroxyProxyService;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import org.controlsfx.control.table.TableRowExpanderColumn;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.boot.WebApplicationType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class ProxyTrafficUI extends Application {

    private TableView<ProxyMessage> trafficTable;
    private TextField filterField;
    private ObservableList<ProxyMessage> messages;
    private FilteredList<ProxyMessage> filteredMessages;
    private ScheduledExecutorService executor;
    private FaroxyProxyService proxyService;
    private ConfigurableApplicationContext springContext;

    @Override
    public void init() {
        springContext = new SpringApplicationBuilder(FaroxyServerApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(getParameters().getRaw().toArray(new String[0]));
        proxyService = springContext.getBean(FaroxyProxyService.class);
    }

    @Override
    public void start(Stage primaryStage) {
        // Ensure we're on the JavaFX Application Thread
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> start(primaryStage));
            return;
        }

        try {
            VBox root = new VBox(10);
            root.setPadding(new Insets(10));

            // Initialize table
            trafficTable = new TableView<>();
            messages = FXCollections.observableArrayList();
            filteredMessages = new FilteredList<>(messages, p -> true);

            // Setup filtering
            filterField = new TextField();
            filterField.setPromptText("Filter messages...");
            filterField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredMessages.setPredicate(message -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    return message.toString().toLowerCase().contains(newValue.toLowerCase());
                });
            });

            // Add components to root
            root.getChildren().addAll(filterField, trafficTable);
            VBox.setVgrow(trafficTable, Priority.ALWAYS);

            Scene scene = new Scene(root, 800, 600);
            primaryStage.setTitle("Faroxy Proxy Traffic");
            primaryStage.setScene(scene);

            // Setup table
            setupTable();

            // Start periodic updates
            startPeriodicUpdates();

            // Show the stage
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Platform.exit();
        }
    }

    private void setupTable() {
        // Timestamp column
        TableColumn<ProxyMessage, String> timestampCol = new TableColumn<>("Timestamp");
        timestampCol.setCellValueFactory(cellData -> cellData.getValue().timestampProperty());
        
        // Direction column
        TableColumn<ProxyMessage, String> directionCol = new TableColumn<>("Type");
        directionCol.setCellValueFactory(cellData -> cellData.getValue().directionProperty());
        
        // Preview column
        TableColumn<ProxyMessage, String> previewCol = new TableColumn<>("Preview");
        previewCol.setCellValueFactory(cellData -> cellData.getValue().previewProperty());
        previewCol.setPrefWidth(400);

        // Details column with expand/collapse
        TableRowExpanderColumn<ProxyMessage> expanderColumn = new TableRowExpanderColumn<>(this::createDetailsNode);
        expanderColumn.setText("Details");
        
        trafficTable.getColumns().setAll(timestampCol, directionCol, previewCol, expanderColumn);
        trafficTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private javafx.scene.Node createDetailsNode(TableRowExpanderColumn.TableRowDataFeatures<ProxyMessage> param) {
        TextArea textArea = new TextArea(param.getValue().getContent());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefRowCount(5);
        return textArea;
    }

    private void startPeriodicUpdates() {
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            if (proxyService != null) {
                Platform.runLater(() -> {
                    try {
                        updateTrafficTable();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    private void updateTrafficTable() {
        // Get latest traffic data
        List<RequestLog> requests = proxyService.getAllRequests();
        List<ResponseLog> responses = proxyService.getAllResponses();

        // Update UI on JavaFX Application Thread
        Platform.runLater(() -> {
            messages.clear();
            // Add requests
            for (RequestLog request : requests) {
                String content = String.format("Method: %s\nURL: %s\nHeaders: %s\nBody: %s",
                    request.getMethod(), request.getUrl(), request.getHeaders(), request.getBody());
                messages.add(new ProxyMessage(content, "REQUEST", request.getTimestamp()));
            }

            // Add responses
            for (ResponseLog response : responses) {
                String content = String.format("Status: %d\nHeaders: %s\nBody: %s",
                    response.getStatusCode(), response.getHeaders(), response.getBody());
                messages.add(new ProxyMessage(content, "RESPONSE", response.getTimestamp()));
            }
            trafficTable.setItems(filteredMessages);
        });
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void copyToClipboard(String content) {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(content);
        clipboard.setContent(clipboardContent);
    }

    @Override
    public void stop() {
        if (executor != null) {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
            }
        }
        springContext.close();
        Platform.exit();
    }

    public static void launch(String[] args) {
        Application.launch(ProxyTrafficUI.class, args);
    }
}
