#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}Starting Faroxy Proxy Server...${NC}"

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "Java is not installed. Please install Java 17 or later."
    exit 1
fi

# Build the project
echo -e "${GREEN}Building project...${NC}"
./gradlew clean build

if [ $? -eq 0 ]; then
    echo -e "${GREEN}Build successful! Starting application...${NC}"
    
    # Run the application with JavaFX configuration
    JAVA_OPTS="--add-modules=javafx.controls,javafx.fxml,javafx.graphics \
               --add-exports=javafx.graphics/com.sun.javafx.tk=ALL-UNNAMED \
               --add-exports=javafx.graphics/com.sun.glass.ui=ALL-UNNAMED"
               
    ./gradlew run --args='--server.port=8080' \
        -Dspring.output.ansi.enabled=ALWAYS \
        -Dlogging.level.io.faroxy=INFO \
        -Dprism.verbose=true \
        -Dprism.order=sw
else
    echo "Build failed. Please check the errors above."
    exit 1
fi
