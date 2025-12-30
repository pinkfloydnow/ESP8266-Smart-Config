#!/usr/bin/env bash
set -euo pipefail

# Default output filename if none provided
OUTPUT_NAME=${1:-"ESP8266-Smart-Config.zip"}

REPO_ROOT=$(git rev-parse --show-toplevel)
cd "$REPO_ROOT"

echo "Creating archive at $OUTPUT_NAME from HEAD..."
git archive --format=zip -o "$OUTPUT_NAME" HEAD

echo "Archive created: $(realpath "$OUTPUT_NAME")"
