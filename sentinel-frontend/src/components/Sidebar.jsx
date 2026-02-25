import { useState } from 'react';

export default function Sidebar({ conversations, onSelect, isOpen, toggleSidebar }) {
  return (
    <div style={{
      width: isOpen ? 220 : 48,
      background: 'rgba(0,0,0,0.85)',
      borderRight: '2px solid #00ff00',
      boxShadow: '0 0 16px #00ff00',
      color: '#00ff00',
      height: '100vh',
      position: 'fixed',
      left: 0,
      top: 0,
      zIndex: 1000,
      transition: 'width 0.3s',
      overflow: 'hidden',
      display: 'flex',
      flexDirection: 'column',
      fontFamily: 'Share Tech Mono, monospace',
      textShadow: '0 0 8px #00ff00',
    }}>
      <button onClick={toggleSidebar} style={{
        background: 'none',
        border: 'none',
        color: '#00ff00',
        fontSize: 24,
        margin: '12px 0',
        cursor: 'pointer',
        alignSelf: 'center',
        filter: 'drop-shadow(0 0 6px #00ff00)'
      }}>
        {isOpen ? '⏴' : '⏵'}
      </button>
      {isOpen && (
        <div style={{ flex: 1, overflowY: 'auto' }}>
          <h3 style={{ margin: '12px 0 8px 16px', fontSize: 16 }}>Conversations</h3>
          {conversations.length === 0 ? (
            <div style={{ marginLeft: 16, fontSize: 14, opacity: 0.7 }}>No history yet.</div>
          ) : (
            conversations.map((conv, idx) => (
              <div key={idx} style={{ padding: '8px 16px', cursor: 'pointer', borderBottom: '1px solid #00ff00', opacity: 0.9 }} onClick={() => onSelect(idx)}>
                {conv.title || `Conversation ${idx + 1}`}
              </div>
            ))
          )}
        </div>
      )}
    </div>
  );
}
