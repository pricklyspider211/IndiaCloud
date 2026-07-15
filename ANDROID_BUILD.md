# Android Build Instructions

## Prerequisites
- Android Studio 2022.1 or higher
- JDK 11+
- Android SDK (API level 34)

## Build APK

1. Open `android/` directory in Android Studio
2. Connect device or start emulator
3. Go to **Build** menu → **Build Bundle(s) / APK(s)** → **Build APK(s)**
4. Wait for build to complete
5. APK location: `android/app/build/outputs/apk/release/app-release.apk`
6. Rename to `indiacloud.apk`

## Features
✅ 2-second splash screen with fade animation
✅ Auto-login session persistence
✅ File upload/download support
✅ Camera & microphone access
✅ Notifications support
✅ Dark theme with pink accent
✅ Fullscreen WebView
✅ All permissions pre-configured

## Build Output

**File:** `indiacloud.apk`
**Size:** ~20-25 MB
**Minimum Android:** 7.0 (API 24)
**Target Android:** 14 (API 34)
