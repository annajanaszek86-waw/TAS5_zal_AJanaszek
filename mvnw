#!/usr/bin/env sh
set -e

MAVEN_VERSION="3.9.9"
PROJECT_DIR="$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)"
MAVEN_DIR="$PROJECT_DIR/.mvn/apache-maven-$MAVEN_VERSION"
MAVEN_CMD="$MAVEN_DIR/bin/mvn"
MAVEN_ZIP="$PROJECT_DIR/.mvn/apache-maven-$MAVEN_VERSION-bin.zip"

if [ ! -x "$MAVEN_CMD" ]; then
  echo "Maven $MAVEN_VERSION was not found in this project. Downloading..."
  mkdir -p "$PROJECT_DIR/.mvn"
  if command -v curl >/dev/null 2>&1; then
    curl -fsSL "https://archive.apache.org/dist/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.zip" -o "$MAVEN_ZIP"
  elif command -v wget >/dev/null 2>&1; then
    wget -q "https://archive.apache.org/dist/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.zip" -O "$MAVEN_ZIP"
  else
    echo "curl or wget is required to download Maven." >&2
    exit 1
  fi
  unzip -q -o "$MAVEN_ZIP" -d "$PROJECT_DIR/.mvn"
fi

exec "$MAVEN_CMD" "$@"
