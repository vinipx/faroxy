#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Default ports
SERVER_PORT=${FAROXY_SERVER_PORT:-8080}
PROXY_PORT=${FAROXY_PROXY_PORT:-8888}

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
    echo -e "${YELLOW}Server port: ${SERVER_PORT}${NC}"
    echo -e "${YELLOW}Proxy port: ${PROXY_PORT}${NC}"
    
    # Run the application with configuration
    ./gradlew run --args="--server.port=${SERVER_PORT}" \
        -Dspring.output.ansi.enabled=ALWAYS \
        -Dlogging.level.io.faroxy=INFO \
        -Dprism.verbose=true \
        -Dprism.order=sw \
        -Dfaroxy.proxy.port=${PROXY_PORT}
else
    echo "Build failed. Please check the errors above."
    exit 1
fi
