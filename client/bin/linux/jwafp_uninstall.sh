#!/bin/bash

APP_NAME="jwafp"
INSTALL_DIR="/usr/local/$APP_NAME"
HANDLER_DESKTOP_FILE="/usr/local/share/applications/${APP_NAME}-handler.desktop"
VERIFIER_DESKTOP_FILE="/usr/local/share/applications/${APP_NAME}-verifier.desktop"

sudo rm -f "$HANDLER_DESKTOP_FILE"
sudo rm -f "$VERIFIER_DESKTOP_FILE"

echo "Uninstalling $APP_NAME..."

sudo rm -rf "$INSTALL_DIR"
sudo rm -f "/usr/local/bin/$APP_NAME"

xdg-mime default "" x-scheme-handler/jwafp
sudo update-desktop-database

echo "Uninstallation complete!"