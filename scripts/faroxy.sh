#!/bin/bash

FAROXY_PORT=8080
# Get the actual IP address
FAROXY_HOST=$(ipconfig getifaddr en0)
if [ -z "$FAROXY_HOST" ]; then
    FAROXY_HOST="localhost"  # Fallback to localhost if IP not found
fi
LOG_DIR="logs"

# Function to check if Faroxy is running
is_running() {
    lsof -i :${FAROXY_PORT} >/dev/null 2>&1
}

# Function to get Faroxy PID
get_pid() {
    lsof -i :${FAROXY_PORT} -t 2>/dev/null
}

# Function to start Faroxy
start() {
    if is_running; then
        echo "Faroxy is already running on port ${FAROXY_PORT}"
    else
        echo "Starting Faroxy..."
        # Create logs directory if it doesn't exist
        cd "$(dirname "$0")/.." 
        mkdir -p ${LOG_DIR}
        
        # Run gradle in quiet mode and redirect output to log file
        ./gradlew bootRun -q > ${LOG_DIR}/faroxy.log 2>&1 &
        
        # Wait for the service to start
        for i in {1..30}; do
            if is_running; then
                echo "✨ Faroxy started successfully!"
                echo "🌐 Configure your browser proxy settings to:"
                echo "   Host: ${FAROXY_HOST}"
                echo "   Port: ${FAROXY_PORT}"
                echo ""
                echo "🔍 Web Interface available at:"
                echo "   http://${FAROXY_HOST}:${FAROXY_PORT}/ui"
                return 0
            fi
            sleep 1
        done
        echo "❌ Failed to start Faroxy. Check logs at ${LOG_DIR}/faroxy.log"
        return 1
    fi
}

# Function to stop Faroxy
stop() {
    if is_running; then
        echo "Stopping Faroxy..."
        kill $(get_pid)
        sleep 2
        if ! is_running; then
            echo "Faroxy stopped successfully"
        else
            echo "Failed to stop Faroxy"
            return 1
        fi
    else
        echo "Faroxy is not running"
    fi
}

# Function to restart Faroxy
restart() {
    stop
    if [ $? -eq 0 ]; then
        start
    fi
}

# Function to show status
status() {
    if is_running; then
        echo "Faroxy is running on port ${FAROXY_PORT}"
        echo "PID: $(get_pid)"
    else
        echo "Faroxy is not running"
    fi
}

# Function to show help
show_help() {
    echo "Usage: $0 {start|stop|restart|status}"
    echo ""
    echo "Commands:"
    echo "  start    Start Faroxy server"
    echo "  stop     Stop Faroxy server"
    echo "  restart  Restart Faroxy server"
    echo "  status   Show Faroxy server status"
    echo ""
}

# Main command handling
case "$1" in
    start)
        start
        ;;
    stop)
        stop
        ;;
    restart)
        restart
        ;;
    status)
        status
        ;;
    *)
        show_help
        exit 1
esac
