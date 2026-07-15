const { app, BrowserWindow, Menu, ipcMain, dialog, session, clipboard, Notification } = require('electron');
const path = require('path');
const isDev = require('electron-is-dev');
const fs = require('fs');
const os = require('os');

let mainWindow;
const userDataPath = app.getPath('userData');
const sessionStorePath = path.join(userDataPath, 'session-storage.json');

const getSessionData = () => {
  try {
    if (fs.existsSync(sessionStorePath)) {
      return JSON.parse(fs.readFileSync(sessionStorePath, 'utf8'));
    }
  } catch (err) {
    console.error('Error reading session:', err);
  }
  return null;
};

const saveSessionData = (data) => {
  try {
    fs.writeFileSync(sessionStorePath, JSON.stringify(data), 'utf8');
  } catch (err) {
    console.error('Error saving session:', err);
  }
};

function createWindow() {
  mainWindow = new BrowserWindow({
    width: 1200,
    height: 800,
    minWidth: 800,
    minHeight: 600,
    icon: path.join(__dirname, '../public/icon.ico'),
    webPreferences: {
      preload: path.join(__dirname, 'preload.js'),
      nodeIntegration: false,
      contextIsolation: true,
      sandbox: true,
      enableRemoteModule: false,
      webSecurity: true,
      allowRunningInsecureContent: false,
      webviewTag: false
    }
  });

  const isDevelopment = !app.isPackaged;
  const url = isDevelopment
    ? 'http://localhost:3000'
    : `file://${path.join(__dirname, '../build/index.html')}`;

  mainWindow.loadURL(url);

  mainWindow.maximize();

  if (isDevelopment) {
    mainWindow.webContents.openDevTools();
  }

  mainWindow.on('closed', () => {
    mainWindow = null;
  });
}

app.on('ready', () => {
  session.defaultSession.webRequest.onBeforeSendHeaders((details, callback) => {
    details.requestHeaders['User-Agent'] = 'IndiaCloudApp/1.0';
    callback({ requestHeaders: details.requestHeaders });
  });

  createWindow();
});

app.on('window-all-closed', () => {
  if (process.platform !== 'darwin') {
    app.quit();
  }
});

app.on('activate', () => {
  if (mainWindow === null) {
    createWindow();
  }
});

if (!isDev) {
  Menu.setApplicationMenu(null);
}

ipcMain.handle('clipboard:write', (event, text) => {
  clipboard.writeText(text);
  return true;
});

ipcMain.handle('clipboard:read', (event) => {
  return clipboard.readText();
});

ipcMain.handle('notification:show', (event, { title, body, icon }) => {
  new Notification({
    title,
    body,
    icon: icon || path.join(__dirname, '../public/icon.png')
  }).show();
  return true;
});

ipcMain.handle('session:save', (event, data) => {
  saveSessionData(data);
  return true;
});

ipcMain.handle('session:load', (event) => {
  return getSessionData();
});

module.exports = { mainWindow };
