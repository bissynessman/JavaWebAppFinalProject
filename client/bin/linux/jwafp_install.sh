#!/bin/bash

APP_NAME="jwafp"
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
INSTALL_DIR="/usr/local/$APP_NAME"
HANDLER_DESKTOP_FILE="/usr/local/share/applications/${APP_NAME}-handler.desktop"
VERIFIER_DESKTOP_FILE="/usr/local/shar/applications/${APP_NAME}-verifier.desktop"

sudo mkdir -p "$INSTALL_DIR"

echo "Installing $APP_NAME..."

sudo cp "$SCRIPT_DIR/bin/jwafp_handler" "$INSTALL_DIR/"
sudo cp "$SCRIPT_DIR/bin/cert.pem" "$INSTALL_DIR/"

sudo chmod +x "$INSTALL_DIR/jwafp_handler"
sudo ln -sf "$INSTALL_DIR/jwafp_handler" "/usr/local/bin/$APP_NAME"

echo "[Desktop Entry]
Name=JWAFP Protocol Handler
Exec=$INSTALL_DIR/jwafp_handler %u
Type=Application
NoDisplay=true
MimeType=x-scheme-handler/jwafp
" | sudo tee "$HANDLER_DESKTOP_FILE" > /dev/null

sudo update-desktop-database
xdg-mime default "$(basename $HANDLER_DESKTOP_FILE)" x-scheme-handler/jwafp

echo "[Desktop Entry]
Name=JWAFP Digital Signature Verifier
Exec=$INSTALL_DIR/jwafp_handler
Type=Application
" | sudo tee "$VERIFIER_DESKTOP_FILE" > /dev/null

echo "Installation complete!"
echo "Use '$APP_NAME' command to run, URLs starting with jwafp:// will launch it."
