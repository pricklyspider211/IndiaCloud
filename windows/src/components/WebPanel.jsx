import React, { useEffect, useRef } from 'react';
import '../styles/WebPanel.css';

function WebPanel() {
  const iframeRef = useRef(null);

  useEffect(() => {
    if (window.electronAPI && window.electronAPI.session) {
      window.electronAPI.session.load().then(data => {
        if (data) {
          console.log('Session loaded');
        }
      });
    }
  }, []);

  useEffect(() => {
    const interval = setInterval(() => {
      if (window.electronAPI && window.electronAPI.session) {
        const sessionData = {
          timestamp: new Date().toISOString(),
          url: window.location.href
        };
        window.electronAPI.session.save(sessionData);
      }
    }, 30000);

    return () => clearInterval(interval);
  }, []);

  return (
    <div className="web-panel-container">
      <iframe
        ref={iframeRef}
        src="https://panel.indiacloud.qzz.io/"
        title="IndiaCloud Panel"
        className="web-panel-iframe"
        sandbox="allow-same-origin allow-scripts allow-forms allow-popups allow-downloads allow-modals"
      />
    </div>
  );
}

export default WebPanel;
