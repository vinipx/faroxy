#!/bin/bash

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Get the absolute path of the Faroxy directory
FAROXY_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

echo -e "${GREEN}Setting up Faroxy...${NC}"

# Make the control script executable
chmod +x "$FAROXY_DIR/scripts/faroxy.sh"
echo -e "${GREEN}✓${NC} Made control script executable"

# Function to add alias to shell config file
add_alias_to_file() {
    local config_file="$1"
    local alias_line="alias faroxy=\"$FAROXY_DIR/scripts/faroxy.sh\""
    
    # Check if alias already exists
    if grep -q "alias faroxy=" "$config_file" 2>/dev/null; then
        # Update existing alias
        sed -i '' "/alias faroxy=/c\\
$alias_line" "$config_file"
        echo -e "${GREEN}✓${NC} Updated Faroxy alias in $config_file"
    else
        # Add new alias
        echo "" >> "$config_file"
        echo "# Faroxy alias" >> "$config_file"
        echo "$alias_line" >> "$config_file"
        echo -e "${GREEN}✓${NC} Added Faroxy alias to $config_file"
    fi
}

# Add alias to shell config files
for config_file in "$HOME/.zshrc" "$HOME/.bashrc" "$HOME/.bash_profile"; do
    if [ -f "$config_file" ]; then
        add_alias_to_file "$config_file"
    fi
done

echo -e "\n${GREEN}Setup completed!${NC}"
echo -e "${YELLOW}Please run the following command to activate the alias:${NC}"
echo -e "    source ~/.zshrc  # if using zsh"
echo -e "    source ~/.bashrc # if using bash"
echo -e "\nYou can now use the 'faroxy' command from anywhere:"
echo -e "    faroxy start     # Start the server"
echo -e "    faroxy stop      # Stop the server"
echo -e "    faroxy restart   # Restart the server"
echo -e "    faroxy status    # Check server status"
echo -e "    faroxy help      # Show help"
