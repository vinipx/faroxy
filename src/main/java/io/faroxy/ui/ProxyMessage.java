package io.faroxy.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProxyMessage {
    private final StringProperty timestamp;
    private final StringProperty direction;
    private final StringProperty preview;
    private final String content;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final int PREVIEW_LENGTH = 100;

    public ProxyMessage(String content, String direction, LocalDateTime timestamp) {
        this.content = content;
        this.direction = new SimpleStringProperty(direction);
        this.timestamp = new SimpleStringProperty(timestamp.format(formatter));
        this.preview = new SimpleStringProperty(generatePreview(content));
    }

    private String generatePreview(String content) {
        if (content == null || content.isEmpty()) {
            return "";
        }
        return content.length() > PREVIEW_LENGTH 
            ? content.substring(0, PREVIEW_LENGTH) + "..." 
            : content;
    }

    public String getContent() {
        return content;
    }

    public StringProperty timestampProperty() {
        return timestamp;
    }

    public StringProperty directionProperty() {
        return direction;
    }

    public StringProperty previewProperty() {
        return preview;
    }
}
