import React, { useState, useEffect } from 'react';
import './App.css';
import SplashScreen from './components/SplashScreen';
import WebPanel from './components/WebPanel';

function App() {
  const [showSplash, setShowSplash] = useState(true);

  useEffect(() => {
    const timer = setTimeout(() => {
      setShowSplash(false);
    }, 2000);

    return () => clearTimeout(timer);
  }, []);

  return (
    <div className="app">
      {showSplash ? (
        <SplashScreen />
      ) : (
        <WebPanel />
      )}
    </div>
  );
}

export default App;
