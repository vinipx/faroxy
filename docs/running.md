# Running Faroxy

## Quick Setup

Faroxy provides a setup script that handles all the necessary configuration:

1. Clone the repository:
   ```bash
   git clone https://github.com/vinipx/faroxy.git
   cd faroxy
   ```

2. Run the setup script:
   ```bash
   ./scripts/setup.sh
   ```

   The setup script will:
   - Make the control script executable
   - Add the `faroxy` command to your shell (zsh and bash supported)
   - Configure necessary permissions
   - Provide clear instructions for next steps

3. Activate the alias (as instructed by the setup script):
   ```bash
   source ~/.zshrc   # if using zsh
   source ~/.bashrc  # if using bash
   ```

4. Start using Faroxy:
   ```bash
   faroxy start    # Start the server
   faroxy status   # Check server status
   faroxy help     # Show all commands
   ```

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
