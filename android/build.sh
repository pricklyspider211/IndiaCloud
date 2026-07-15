#!/usr/bin/env bash
# Convenience script to build the IndiaCloud Android APK on Replit.
# Usage: bash android/build.sh [assembleDebug|assembleRelease]
set -e
cd "$(dirname "$0")"

export JAVA_HOME="$(dirname "$(dirname "$(readlink -f "$(command -v java)")")")"
export ANDROID_HOME="$(cd .. && pwd)/.cache/android-sdk"
export PATH="$JAVA_HOME/bin:$PATH"

TASK="${1:-assembleRelease}"
./gradlew "$TASK" --console=plain

echo ""
echo "Build finished. APK output:"
find app/build/outputs/apk -name "*.apk"
