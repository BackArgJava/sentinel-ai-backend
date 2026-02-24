import { useState } from 'react';

export default function Terminal() {
  const [input, setInput] = useState('');
  // This state holds the history of the chat
  const [messages, setMessages] = useState([
    { sender: 'system', text: 'System initialized. Waiting for command...' }
  ]);

  const handleSendMessage = async (e) => {
    e.preventDefault();
    if (!input.trim()) return;

    // 1. Instantly print the Agent's message to the screen
    const userMsg = { sender: 'user', text: input };
    setMessages((prev) => [...prev, userMsg]);
    setInput(''); // clear the input box

    try {
      // 2. Grab the VIP Wristband (JWT) from storage
      const token = localStorage.getItem('sentinel_token');

      // 3. Send the transmission to the Mainframe
      // (We are assuming your backend AI controller listens at /api/commands)
      const response = await fetch('http://localhost:8081/api/commands/execute', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}` // Presenting the wristband to Spring Security!
        },
        body: JSON.stringify({ command: userMsg.text })
      });

      if (response.ok) {
        const data = await response.json();
        // 4. Print the AI's response
        // Note: adjust "data.reply" depending on what your backend actually sends back
        setMessages((prev) => [...prev, { sender: 'system', text: data.reply || data.response || "Command received." }]);
      } else {
        setMessages((prev) => [...prev, { sender: 'system', text: '[!] ERROR: Connection to AI Core failed. Access Denied.' }]);
      }
    } catch (error) {
      setMessages((prev) => [...prev, { sender: 'system', text: '[!] CRITICAL: Mainframe offline.' }]);
    }
  };

  return (
    <div style={{ backgroundColor: '#111', color: '#00ff00', padding: '20px', height: '100vh', fontFamily: 'monospace', display: 'flex', flexDirection: 'column' }}>
      <h2>[ SENTINEL AI - NEURAL LINK ACTIVE ]</h2>
      
      {/* The Chat Window */}
      <div style={{ flex: 1, overflowY: 'auto', marginBottom: '20px', border: '1px solid #00ff00', padding: '10px', backgroundColor: '#000' }}>
        {messages.map((msg, index) => (
          <p key={index} style={{ margin: '10px 0', color: msg.sender === 'user' ? '#ffffff' : '#00ff00' }}>
            {msg.sender === 'user' ? 'AGENT: ' : 'SENTINEL: '} {msg.text}
          </p>
        ))}
      </div>

      {/* The Input Box */}
      <form onSubmit={handleSendMessage} style={{ display: 'flex' }}>
        <span style={{ marginRight: '10px', fontSize: '18px', lineHeight: '30px' }}>&gt;</span>
        <input 
          type="text" 
          value={input} 
          onChange={(e) => setInput(e.target.value)} 
          autoFocus
          style={{ flex: 1, backgroundColor: 'transparent', color: '#00ff00', border: 'none', borderBottom: '1px solid #00ff00', outline: 'none', fontFamily: 'monospace', fontSize: '18px' }}
        />
        <button type="submit" style={{ backgroundColor: '#00ff00', color: '#111', border: 'none', padding: '0 20px', marginLeft: '10px', cursor: 'pointer', fontWeight: 'bold', fontFamily: 'monospace', fontSize: '16px' }}>
          TRANSMIT
        </button>
      </form>
    </div>
  );
}