# Windows Build Instructions

## Prerequisites
- Node.js 16+ (https://nodejs.org/)
- npm or yarn
- Windows 7+ (x64)

## Setup

1. Navigate to windows directory:
```bash
cd windows
```

2. Install dependencies:
```bash
npm install
```

## Development Mode

```bash
npm run dev
```

This will:
- Start React development server on http://localhost:3000
- Launch Electron app connected to dev server
- Show DevTools for debugging

## Production Build

### Build for Distribution:

```bash
npm run dist
```

This will:
1. Build React production bundle
2. Package with Electron Builder
3. Generate installer: `windows/dist/IndiaCloud-Setup.exe`
4. Generate portable exe: `windows/dist/IndiaCloud.exe`

### Build Output Files

- **Installer:** `windows/dist/IndiaCloud-Setup.exe` (Recommended)
- **Portable:** `windows/dist/IndiaCloud.exe` (No installation needed)
- **Rename installer to:** `indiacloud.exe`

## Features
✅ 2-second splash screen
✅ Session persistence (auto-login)
✅ File upload/download support
✅ Notifications support
✅ Clipboard support
✅ Dark theme
✅ Full https://panel.indiacloud.qzz.io/ support
✅ No address bar or menus
✅ Auto-maximize on first launch

## Build Size

- Installer: ~150-200 MB (includes Node.js runtime)
- Portable: ~180-220 MB

## Troubleshooting

**Port 3000 already in use:**
```bash
npm run dist-only
```

**Build stuck:**
```bash
rm -rf node_modules build dist
npm install
npm run dist
```

**Icon not showing:**
Ensure `windows/public/icon.ico` and `windows/public/icon.png` exist
