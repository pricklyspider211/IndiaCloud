# IndiaCloud - Professional Web Panel Wrapper

A dual-platform application providing native Android and Windows desktop wrappers for the IndiaCloud web panel.

## Quick Start

### Android APK Build
```bash
cd android
./gradlew assembleRelease
# Output: android/app/build/outputs/apk/release/app-release.apk
# Rename to: indiacloud.apk
```

### Windows EXE Build
```bash
cd windows
npm install
npm run dist
# Output: windows/dist/IndiaCloud-Setup.exe
# Rename to: indiacloud.exe
```

## Features

✅ Splash screen (2 seconds fade animation)
✅ Auto-login session persistence
✅ File upload/download support
✅ Notifications support
✅ Camera & Microphone access
✅ Clipboard support
✅ Dark theme with pink accent
✅ No browser UI/address bar
✅ Production-ready builds
✅ Full https://panel.indiacloud.qzz.io/ support

## Platform Support

- **Android**: Minimum SDK 24 (Android 7.0), Target SDK 34
- **Windows**: Windows 7+ (x64)

## Panel URL

**Production:** https://panel.indiacloud.qzz.io/

## Build Outputs

- **Android:** `indiacloud.apk`
- **Windows:** `indiacloud.exe`