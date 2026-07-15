import React, { useEffect } from 'react';
import '../styles/SplashScreen.css';

function SplashScreen() {
  useEffect(() => {
    const logoElement = document.getElementById('splash-logo');
    if (logoElement) {
      logoElement.style.animation = 'fadeIn 0.5s ease-in';
    }
  }, []);

  return (
    <div className="splash-container">
      <svg width="200" height="200" viewBox="0 0 200 200" className="splash-logo" id="splash-logo">
        <defs>
          <linearGradient id="cloudGradient" x1="0%" y1="0%" x2="100%" y2="100%">
            <stop offset="0%" style={{stopColor: '#00bfff', stopOpacity: 1}} />
            <stop offset="100%" style={{stopColor: '#ff69b4', stopOpacity: 1}} />
          </linearGradient>
        </defs>
        <circle cx="100" cy="100" r="95" fill="none" stroke="url(#cloudGradient)" strokeWidth="8" opacity="0.3"/>
        <circle cx="70" cy="110" r="40" fill="#00bfff" opacity="0.8"/>
        <circle cx="130" cy="110" r="40" fill="#00bfff" opacity="0.8"/>
        <circle cx="100" cy="90" r="45" fill="#00bfff" opacity="0.8"/>
        <text x="100" y="160" fontSize="24" fontWeight="bold" fill="#ff1493" textAnchor="middle">Cloud</text>
      </svg>
      <h1 className="splash-text">IndiaCloud</h1>
    </div>
  );
}

export default SplashScreen;
