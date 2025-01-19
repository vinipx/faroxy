#!/bin/bash

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m' # No Color

echo -e "${RED}Stopping Faroxy Proxy Server...${NC}"

# Find Java process running Faroxy
PID=$(ps aux | grep "[i]o.faroxy.FaroxyServerApplication" | awk '{print $2}')

if [ -z "$PID" ]; then
    echo -e "${GREEN}No Faroxy process found running.${NC}"
    exit 0
fi

# Try graceful shutdown first
echo -e "Attempting graceful shutdown of PID: $PID"
kill -15 $PID

# Wait for up to 5 seconds for graceful shutdown
count=0
while [ $count -lt 5 ]; do
    if ! ps -p $PID > /dev/null; then
        echo -e "${GREEN}Faroxy server stopped successfully.${NC}"
        exit 0
    fi
    sleep 1
    count=$((count + 1))
done

# Force kill if still running
if ps -p $PID > /dev/null; then
    echo -e "${RED}Graceful shutdown failed. Force killing process...${NC}"
    kill -9 $PID
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}Faroxy server force stopped.${NC}"
    else
        echo -e "${RED}Failed to stop Faroxy server. Please check manually.${NC}"
        exit 1
    fi
fi
