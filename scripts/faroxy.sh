#!/bin/bash

# Default ports
FAROXY_SERVER_PORT=9090
FAROXY_PROXY_PORT=9999

# Default host
FAROXY_HOST=$(ipconfig getifaddr en0 2>/dev/null || echo "localhost")

# Directories
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"
LOG_DIR="${PROJECT_DIR}/logs"

# Function to check if Faroxy is running
is_running() {
    lsof -i :${FAROXY_SERVER_PORT} -t 2>/dev/null
}

# Function to get PID
get_pid() {
    lsof -i :${FAROXY_SERVER_PORT} -t 2>/dev/null
}

# Function to stop Faroxy
stop() {
    local pid=$(get_pid)
    if [ -n "$pid" ]; then
        echo "Stopping Faroxy (PID: $pid)..."
        kill $pid
        # Wait for the process to stop
        for i in {1..30}; do
            if ! is_running; then
                echo "✨ Faroxy stopped successfully!"
                return 0
            fi
            sleep 1
        done
        # Force kill if still running
        echo "Force stopping Faroxy..."
        kill -9 $pid 2>/dev/null
        sleep 1
    else
        echo "Faroxy is not running"
    fi
}

# Function to start Faroxy
start() {
    if is_running; then
        echo "Faroxy is already running on port ${FAROXY_SERVER_PORT}"
        return 1
    else
        echo "Starting Faroxy on port ${FAROXY_SERVER_PORT}..."
        # Create logs directory if it doesn't exist
        cd "${PROJECT_DIR}"
        mkdir -p "${LOG_DIR}"
        
        # Run gradle with environment variables and local profile
        export FAROXY_SERVER_PORT=${FAROXY_SERVER_PORT}
        export FAROXY_PROXY_PORT=${FAROXY_PROXY_PORT}
        
        # Run in background and redirect output to log file
        nohup ./gradlew bootRun --console=plain --args='--spring.profiles.active=local' > "${LOG_DIR}/faroxy.log" 2>&1 &
        
        # Wait for the service to start
        for i in {1..30}; do
            if is_running; then
                echo "✨ Faroxy started successfully!"
                echo "🌐 Proxy server running at:"
                echo "   Host: ${FAROXY_HOST}"
                echo "   Port: ${FAROXY_PROXY_PORT}"
                echo ""
                echo "🔍 Web Interface available at:"
                echo "   http://${FAROXY_HOST}:${FAROXY_SERVER_PORT}"
                echo ""
                echo "📝 Logs available at: ${LOG_DIR}/faroxy.log"
                return 0
            fi
            sleep 1
        done
        echo "❌ Failed to start Faroxy. Check logs at ${LOG_DIR}/faroxy.log"
        return 1
    fi
}

# Function to restart Faroxy
restart() {
    echo "Restarting Faroxy..."
    stop
    sleep 2  # Give it some time to fully stop
    start
}

# Function to show status
status() {
    if is_running; then
        echo "Faroxy is running on port ${FAROXY_SERVER_PORT}"
        echo "PID: $(get_pid)"
    else
        echo "Faroxy is not running"
    fi
}

# Function to show help
help() {
    echo "Usage: $0 {start|stop|restart|status|help}"
    echo ""
    echo "Commands:"
    echo "  start     Start Faroxy server"
    echo "  stop      Stop Faroxy server"
    echo "  restart   Restart Faroxy server"
    echo "  status    Show Faroxy server status"
    echo "  help      Show this help message"
}

# Main script
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
    help)
        help
        ;;
    *)
        echo "Usage: $0 {start|stop|restart|status|help}"
        exit 1
        ;;
esac
