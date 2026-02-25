// ==========================================
// 1. ALL IMPORTS MUST GO AT THE VERY TOP
// ==========================================
import { useState, useRef, useEffect } from 'react';
import Sidebar from './Sidebar'; 
import agentIcon from './assets/agent.svg'; 
import sentinelIcon from './assets/sentinel.svg'; 
import ReactMarkdown from 'react-markdown';
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { vscDarkPlus } from 'react-syntax-highlighter/dist/esm/styles/prism';

// ==========================================
// 2. THE MAIN COMPONENT WRAPPER
// ==========================================
export default function Terminal() {
  
  // ==========================================
  // 3. ALL STATE (HOOKS) MUST BE INSIDE THE FUNCTION
  // ==========================================
  const [sidebarOpen, setSidebarOpen] = useState(true);
  const [conversations, setConversations] = useState([
    { title: 'Debugged Controller' },
    { title: 'System Check' }
  ]);
  const [input, setInput] = useState('');
  const [messages, setMessages] = useState([
    { sender: 'system', text: 'System initialized. Waiting for command...' }
  ]);
  const [isProcessing, setIsProcessing] = useState(false);

  // ==========================================
  // 4. FUNCTIONS AND LOGIC
  // ==========================================
  const handleSelectConversation = (idx) => {
    // TODO: Load conversation by idx
  };
  
  const toggleSidebar = () => setSidebarOpen((open) => !open);

  const handleSendMessage = async (e) => {
    e.preventDefault();
    if (!input.trim() || isProcessing) return;
    
    const userMsg = { sender: 'user', text: input };
    setMessages((prev) => [...prev, userMsg]);
    setInput('');
    setIsProcessing(true);
    
    try {
      const token = localStorage.getItem('sentinel_token');
      const response = await fetch('http://localhost:8081/api/commands/execute', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({ command: userMsg.text })
      });
      
      if (response.ok) {
        const data = await response.json();
        setMessages((prev) => [...prev, { sender: 'system', text: data.response || "Command received." }]);
      } else {
        setMessages((prev) => [...prev, { sender: 'system', text: '[!] ERROR: Connection to AI Core failed. Access Denied.' }]);
      }
    } catch (error) {
      setMessages((prev) => [...prev, { sender: 'system', text: '[!] CRITICAL: Mainframe offline.' }]);
    } finally {
      setIsProcessing(false);
    }
  };

  // ==========================================
  // 5. THE RETURN (UI RENDER)
  // ==========================================
  return (
    <>
      <Sidebar
        conversations={conversations}
        onSelect={handleSelectConversation}
        isOpen={sidebarOpen}
        toggleSidebar={toggleSidebar}
      />
      <div className="crt-scanlines" />
      <div style={{ backgroundColor: '#111', color: '#00ff00', padding: '20px', height: '100vh', fontFamily: 'Share Tech Mono, monospace', display: 'flex', flexDirection: 'column', textShadow: '0 0 8px #00ff00', marginLeft: sidebarOpen ? 220 : 48, transition: 'margin-left 0.3s' }}>
        <h2>[ SENTINEL AI - NEURAL LINK ACTIVE ]</h2>
        
        {/* The Chat Window */}
        <div className="chat-window" style={{ flex: 1, overflowY: 'auto', marginBottom: '20px', border: '1px solid #00ff00', padding: '10px', backgroundColor: '#000' }}>
          {messages.map((msg, index) => (
            <div key={index} style={{ display: 'flex', alignItems: 'flex-start', margin: '10px 0' }}>
              
              {/* Avatars */}
              <img
                src={msg.sender === 'user' ? agentIcon : sentinelIcon}
                alt={msg.sender === 'user' ? 'Agent Avatar' : 'Sentinel Avatar'}
                style={{ width: 32, height: 32, marginRight: 12, filter: 'drop-shadow(0 0 6px #00ff00)' }}
              />
              
              <div style={{ flex: 1 }}>
                {/* Username and Clearance Badges */}
                <div style={{ marginBottom: '5px' }}>
                  <span style={{ color: msg.sender === 'user' ? '#ffffff' : '#00ff00', fontWeight: 'bold', marginRight: 8 }}>
                    {msg.sender === 'user' ? 'AGENT SANTI' : 'SENTINEL'}
                  </span>
                  {msg.sender === 'user' && (
                    <span style={{ background: 'rgba(0,255,0,0.15)', color: '#00ff00', borderRadius: 4, padding: '2px 8px', fontSize: '12px', fontWeight: 'bold', marginRight: 8, boxShadow: '0 0 4px #00ff00' }}>
                      [ CLEARANCE: CLASS-1 ]
                    </span>
                  )}
                </div>

                {/* Message Body with Markdown formatting */}
                {msg.sender === 'system' ? (
                  <ReactMarkdown
                    children={msg.text}
                    components={{
                      code({node, inline, className, children, ...props}) {
                        const match = /language-(\w+)/.exec(className || '');
                        return !inline && match ? (
                          <SyntaxHighlighter style={vscDarkPlus} language={match[1]} PreTag="div" {...props}>
                            {String(children).replace(/\n$/, '')}
                          </SyntaxHighlighter>
                        ) : (
                          <code className={className} {...props}>{children}</code>
                        );
                      }
                    }}
                  />
                ) : (
                  <span style={{ color: '#ffffff' }}>{msg.text}</span>
                )}
              </div>
            </div>
          ))}
          
          {/* Loading Indicator */}
          {isProcessing && (
            <p style={{ margin: '10px 0', color: '#00ff00', fontStyle: 'italic' }}>
              SENTINEL: Processing request...
            </p>
          )}
        </div>

        {/* The Input Box */}
        <form onSubmit={handleSendMessage} style={{ display: 'flex' }}>
          <span style={{ marginRight: '10px', fontSize: '18px', lineHeight: '30px' }}>&gt;</span>
          <input
            type="text"
            value={input}
            onChange={(e) => setInput(e.target.value)}
            disabled={isProcessing}
            autoFocus
            className="glass-input"
            style={{ flex: 1, backgroundColor: 'transparent', color: '#00ff00', border: 'none', borderBottom: '1px solid #00ff00', outline: 'none', fontFamily: 'monospace', fontSize: '18px', opacity: isProcessing ? 0.5 : 1 }}
          />
          <button type="submit" disabled={isProcessing} style={{ backgroundColor: isProcessing ? '#555' : '#00ff00', color: '#111', border: 'none', padding: '0 20px', marginLeft: '10px', cursor: isProcessing ? 'not-allowed' : 'pointer', fontWeight: 'bold', fontFamily: 'monospace', fontSize: '16px' }}>
            TRANSMIT
          </button>
        </form>
      </div>
    </>
  );
}