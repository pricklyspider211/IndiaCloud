# IndiaCloud - Web Panel Wrapper

## Overview
Imported GitHub project providing native app wrappers around the IndiaCloud
web panel (https://panel.indiacloud.qzz.io/):

- `android/` — native Android app (Kotlin, WebView-based) that loads the panel.
- `windows/` — Electron desktop wrapper for the same panel.

This is not a typical web app — there is no server to preview in Replit. The
Android build is now set up and buildable in this workspace; the Windows
Electron build was not touched.

## Building the Android APK in Replit
Toolchain installed via Nix: `temurin-bin-17` (JDK) and `gradle` (used once to
generate the Gradle wrapper — the checked-in `android/gradlew` now pins Gradle
8.4, compatible with the project's Android Gradle Plugin 8.0.0).

The Android SDK (platform-tools, `platforms;android-34`, `build-tools;34.0.0`)
was downloaded via the SDK command-line tools into `.cache/android-sdk`
(gitignored, not committed — regenerate with the commands below if missing).

To build:
```bash
bash android/build.sh assembleRelease   # or assembleDebug
```
Output APK: `android/app/build/outputs/apk/release/app-release.apk` (or `debug/`).

If `.cache/android-sdk` is missing (e.g. fresh clone), recreate it:
```bash
mkdir -p .cache/android-sdk/cmdline-tools
curl -sSL -o /tmp/cmdline-tools.zip https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip
unzip -q /tmp/cmdline-tools.zip -d .cache/android-sdk/cmdline-tools
mv .cache/android-sdk/cmdline-tools/cmdline-tools .cache/android-sdk/cmdline-tools/latest
yes | .cache/android-sdk/cmdline-tools/latest/bin/sdkmanager --sdk_root=.cache/android-sdk \
  "platform-tools" "platforms;android-34" "build-tools;34.0.0"
echo "sdk.dir=$(pwd)/.cache/android-sdk" > android/local.properties
```

### Fixes made to get the build working
- `android/gradle.properties`: removed the obsolete `-XX:MaxPermSize` JVM flag
  (unsupported on modern JDKs, crashed the Gradle daemon).
- Generated `android/gradlew`/`gradlew.bat` (the wrapper jar was missing from
  the import).
- Generated a debug keystore at `android/app/debug.keystore` (referenced by
  the release signing config but not present in the import).
- Added missing launcher icons (`res/mipmap-*/ic_launcher*.png`, placeholder
  monogram) and a splash logo (`res/drawable/ic_logo.xml`) — the layouts
  referenced these but no icon assets were included in the import. Replace
  these with real branded assets when available.
- Fixed `android/app/src/main/res/xml/data_extraction_rules.xml`, which had
  invalid `<domain-config>` content (that belongs in a network security
  config, not data-extraction-rules) and failed lint.
- Made `FILE_PICKER_REQUEST_CODE` / `CAMERA_PERMISSION_REQUEST_CODE` in
  `MainActivity.kt` non-private — they're accessed from `IndiaCloudWebChromeClient`
  and didn't compile as `private`.

## Building the Windows Electron app in Replit
Node deps for `windows/` are installed. Build steps:
```bash
cd windows
npm install        # already done once; safe to rerun
npm run build       # CRA production build -> windows/build
npm run dist         # electron-builder -> windows/dist
```

### Fixes made to get the Electron build working
- `windows/electron-builder.json`: added `"extends": null`. electron-builder
  auto-detects `react-scripts` in `devDependencies` and silently applies its
  built-in `react-cra` preset, which force-overrides the packaged app's entry
  point to `build/electron.js` (ignoring `package.json`'s `main` field and
  this project's own `electron/**/*` files list). That caused
  `npm run dist` to fail with "Application entry file build/electron.js ...
  does not exist" even though `package.json` correctly points at
  `electron/main.js`. Setting `extends: null` disables the preset so the
  project's own config wins.
- Real IndiaCloud branding (`attached_assets/indi_1784123860845.png`) is now
  wired in everywhere a placeholder used to be:
  - Android: all `mipmap-*/ic_launcher.png` / `ic_launcher_round.png`
    densities regenerated from the real logo, plus a proper adaptive icon
    (`mipmap-anydpi-v26/ic_launcher.xml`, foreground PNGs, `#0A0E27`
    background) for Android 8+. The splash screen (`activity_splash.xml`)
    now shows the real logo (`drawable-nodpi/ic_logo.png`) instead of the
    hand-drawn placeholder vector.
  - Windows: `windows/public/icon.ico` (multi-resolution) and
    `windows/public/favicon.ico` generated from the same logo. electron-builder
    auto-picks up `icon.ico` from `buildResources` (`public/`), and CRA uses
    `favicon.ico` for the app's browser tab icon during `npm run build`.
  - Note: embedding that icon into the actual packaged `.exe`'s resources
    happens via `rcedit`, which runs natively on Windows/macOS but needs Wine
    on Linux — so a `--win dir` test build made *inside this Replit sandbox*
    will still show the default Electron icon on the exe file itself, even
    though `icon.ico` is correctly configured. This resolves itself
    automatically on the `windows-latest` GitHub Actions runner (no Wine
    involved there) or when built directly on Windows.

### Known limitation: full NSIS installer can't be built inside Replit
Verified: `npm run build` and `electron-builder --win dir` (produces the
unpacked app in `windows/dist/win-unpacked/`, with the correct
`electron/main.js` entry point baked into `app.asar`) both complete
successfully in this workspace.

Building the actual installer (`npm run dist`, target `nsis`) fails at the
very last step — after `makensis` (installed via Nix) successfully compiles
the installer — because electron-builder always launches the freshly-built
`.exe` once under Wine to extract the uninstaller. Replit's container has no
32-bit executable support at all (confirmed: even `wine --version` fails
with "Exec format error", and compiling/running a trivial 32-bit ELF binary
fails the same way), so this Wine invocation cannot work here. This is a
sandbox-level restriction, not a project misconfiguration — the same
`npm run dist` command builds the full signed installer without any changes
in a normal Linux CI runner, macOS, or on Windows directly.

The repo already has a GitHub Actions workflow
(`.github/workflows/build.yml`) that builds the Windows EXE on
`windows-latest` (no Wine needed there) and the Android APK on
`ubuntu-latest` — that's the intended path to a distributable
`indiacloud.exe`. Use `npm run dist` here in Replit only to validate the app
packages correctly (via `--win dir`); build the final installer via that
workflow or a real Windows machine.

## User preferences
(none recorded yet)
