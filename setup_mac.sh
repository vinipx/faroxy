#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}Setting up Faroxy Proxy Server...${NC}"

# Get the current directory
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Make start and stop scripts executable
chmod +x "$SCRIPT_DIR/start.sh"
chmod +x "$SCRIPT_DIR/stop.sh"

# Determine which shell configuration file to use
SHELL_CONFIG=""
if [ -f "$HOME/.zshrc" ]; then
    SHELL_CONFIG="$HOME/.zshrc"
elif [ -f "$HOME/.bashrc" ]; then
    SHELL_CONFIG="$HOME/.bashrc"
elif [ -f "$HOME/.bash_profile" ]; then
    SHELL_CONFIG="$HOME/.bash_profile"
else
    echo -e "${YELLOW}No shell configuration file found. Creating .zshrc${NC}"
    SHELL_CONFIG="$HOME/.zshrc"
    touch "$SHELL_CONFIG"
fi

# Check if aliases already exist
if ! grep -q "alias proxystart=" "$SHELL_CONFIG" && ! grep -q "alias proxystop=" "$SHELL_CONFIG"; then
    echo -e "\n# Faroxy Proxy Server aliases" >> "$SHELL_CONFIG"
    echo "alias proxystart=\"cd $SCRIPT_DIR && ./start.sh\"" >> "$SHELL_CONFIG"
    echo "alias proxystop=\"cd $SCRIPT_DIR && ./stop.sh\"" >> "$SHELL_CONFIG"
    echo -e "${GREEN}Aliases added successfully!${NC}"
else
    echo -e "${YELLOW}Aliases already exist in $SHELL_CONFIG${NC}"
fi

# Source the configuration file
echo -e "${GREEN}Reloading shell configuration...${NC}"
source "$SHELL_CONFIG"

echo -e "${GREEN}Setup complete! You can now use the following commands:${NC}"
echo -e "  ${YELLOW}proxystart${NC} - Start the Faroxy Proxy Server"
echo -e "  ${YELLOW}proxystop${NC}  - Stop the Faroxy Proxy Server"
echo -e "\n${GREEN}Note: You may need to restart your terminal or run 'source $SHELL_CONFIG' for the changes to take effect.${NC}"
