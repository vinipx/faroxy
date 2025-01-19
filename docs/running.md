# Running Faroxy

Faroxy provides multiple ways to run and manage the application. This guide covers all the available options and best practices.

## Using the Control Script

Faroxy comes with a convenient shell script (`scripts/faroxy.sh`) that makes it easy to manage the application.

### Basic Usage

1. Make the script executable (one-time setup):
   ```bash
   chmod +x scripts/faroxy.sh
   ```

2. Available commands:
   ```bash
   ./scripts/faroxy.sh start    # Start the Faroxy server
   ./scripts/faroxy.sh stop     # Stop the server
   ./scripts/faroxy.sh restart  # Restart the server
   ./scripts/faroxy.sh status   # Check server status
   ./scripts/faroxy.sh help     # Show help message
   ```

### Setting up an Alias

For easier access, you can set up an alias in your shell configuration:

1. Add the alias to your shell configuration file (`.bashrc` or `.zshrc`):
   ```bash
   echo 'alias faroxy="$HOME/path/to/faroxy/scripts/faroxy.sh"' >> ~/.zshrc
   ```

2. Reload your shell configuration:
   ```bash
   source ~/.zshrc
   ```

3. Now you can use the `faroxy` command from anywhere:
   ```bash
   faroxy start
   faroxy status
   ```

### Script Features

The control script provides several helpful features:

- **Port Checking**: Automatically checks if port 8080 is available
- **Process Management**: Safely starts and stops the application
- **Status Monitoring**: Shows if the application is running and its PID
- **Clean Restart**: Ensures proper shutdown before starting

## Manual Running

If you prefer not to use the control script, you can run Faroxy manually:

### Using Gradle

```bash
# Run the application
./gradlew bootRun

# Build the application
./gradlew build

# Run tests
./gradlew test
```

### Using Java

After building, you can run the JAR file directly:

```bash
java -jar build/libs/faroxy-<version>.jar
```

## Docker Support

Faroxy also supports running in a Docker container. See our [Docker Guide](docker.md) for details.

## Troubleshooting

### Common Issues

1. **Port Already in Use**
   ```bash
   # Check what's using port 8080
   lsof -i :8080
   
   # Kill the process
   kill <PID>
   ```

2. **Permission Issues**
   ```bash
   # Make sure the script is executable
   chmod +x scripts/faroxy.sh
   ```

3. **Java Version**
   ```bash
   # Verify Java version
   java -version
   ```

### Getting Help

If you encounter any issues:
1. Check the application logs in `logs/faroxy.log`
2. Use `faroxy status` to check the application state
3. Visit our [GitHub Issues](https://github.com/vinipx/faroxy/issues) page
4. Refer to the [Troubleshooting Guide](troubleshooting.md)
