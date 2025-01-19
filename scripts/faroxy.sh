#!/bin/bash

FAROXY_PORT=8080

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
        ./gradlew bootRun &
        sleep 5
        if is_running; then
            echo "Faroxy started successfully"
        else
            echo "Failed to start Faroxy"
        fi
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
        fi
    else
        echo "Faroxy is not running"
    fi
}

# Function to restart Faroxy
restart() {
    if is_running; then
        stop
    fi
    start
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
    echo "Faroxy Control Script"
    echo "Usage: faroxy [command]"
    echo ""
    echo "Commands:"
    echo "  start    Start Faroxy server"
    echo "  stop     Stop Faroxy server"
    echo "  restart  Restart Faroxy server"
    echo "  status   Show Faroxy server status"
    echo "  help     Show this help message"
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
    help|--help|-h)
        show_help
        ;;
    *)
        echo "Unknown command: $1"
        echo "Use 'faroxy help' to see available commands"
        exit 1
        ;;
esac
