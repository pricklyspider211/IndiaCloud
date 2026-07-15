# IndiaCloud Complete Build Guide

## Project Structure

```
IndiaCloud/
├── android/                    # Android Native App
│   ├── app/src/main/
│   ├── build.gradle.kts
│   └── settings.gradle.kts
├── windows/                    # Windows Desktop App
│   ├── src/
│   ├── electron/
│   ├── public/
│   ├── package.json
│   └── electron-builder.json
├── ANDROID_BUILD.md
├── WINDOWS_BUILD.md
└── README.md
```

## Final Outputs

### Android: `indiacloud.apk`
- **Location:** `android/app/build/outputs/apk/release/app-release.apk`
- **Rename to:** `indiacloud.apk`
- **Size:** ~20-25 MB
- **Min Android:** 7.0 (API 24)
- **Target Android:** 14 (API 34)

### Windows: `indiacloud.exe`
- **Location:** `windows/dist/IndiaCloud-Setup.exe`
- **Rename to:** `indiacloud.exe`
- **Size:** ~150-200 MB
- **OS:** Windows 7+
- **Architecture:** x64

## Quick Commands

### Android
```bash
cd android
./gradlew assembleRelease
# Output: android/app/build/outputs/apk/release/app-release.apk → indiacloud.apk
```

### Windows
```bash
cd windows
npm install
npm run dist
# Output: windows/dist/IndiaCloud-Setup.exe → indiacloud.exe
```

## Both Apps Load

**URL:** https://panel.indiacloud.qzz.io/

✅ No website creation
✅ Pure wrappers only
✅ Session persistence
✅ Full feature support
