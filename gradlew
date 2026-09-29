#!/bin/sh
# Minimal pinned Gradle launcher. Requires JDK 17, curl, unzip and sha256sum.
set -eu
VERSION=8.11.1
SHA=f397b287023acdba1e9f6fc5ea72d22dd63669d59ed4a289a29b1a76eee151c6
CACHE="${GRADLE_USER_HOME:-$HOME/.gradle}/yuki-distributions"
mkdir -p "$CACHE"
if [ ! -x "$CACHE/gradle-$VERSION/bin/gradle" ]; then
  TMP=$(mktemp -d "$CACHE/install.XXXXXX")
  trap 'rm -rf "$TMP"' EXIT HUP INT TERM
  curl --fail --location --retry 3 "https://services.gradle.org/distributions/gradle-$VERSION-bin.zip" -o "$TMP/gradle.zip"
  printf '%s  %s\n' "$SHA" "$TMP/gradle.zip" | sha256sum -c -
  unzip -q "$TMP/gradle.zip" -d "$TMP"
  mv "$TMP/gradle-$VERSION" "$CACHE/gradle-$VERSION"
fi
exec "$CACHE/gradle-$VERSION/bin/gradle" "$@"
