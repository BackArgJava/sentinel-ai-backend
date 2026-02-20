import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

function Dashboard() {
  const navigate = useNavigate();
  
  // State to hold the chat history
  const [messages, setMessages] = useState([
    { sender: 'System', text: 'Awaiting AI integration...', color: 'yellow' },
    { sender: 'Sentinel', text: 'Standing by for commands.', color: '#0088ff' }
  ]);
  
  // State to hold the user's current input
  const [input, setInput] = useState('');

  const handleLogout = () => {
    // 1. Destroy the VIP pass!
    localStorage.removeItem('sentinelToken');
    
    // 2. Kick the user back to the login screen
    navigate('/');
  };

  const handleSendCommand = (e) => {
    e.preventDefault();
    if (!input.trim()) return;

    // 1. Show the user's command on the screen immediately
    const newMessages = [...messages, { sender: 'Agent', text: input, color: '#00ff88' }];
    setMessages(newMessages);
    setInput('');

    // 2. Grab the VIP Pass from the vault
    const token = localStorage.getItem('sentinelToken');

    // 3. Send the command to the Java Brain (Docker)
    fetch('http://localhost:8081/api/commands/execute', {
      method: 'POST',
      headers: { 
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}` 
      },
      body: JSON.stringify({ command: input })
    })
    .then(response => {
      if (response.status === 403 || response.status === 401) {
        throw new Error("UNAUTHORIZED! The Bouncer rejected the token.");
      }
      return response.text(); 
    })
    .then(data => {
      // 4. Print Java's response to the screen
      setMessages(prev => [...prev, { sender: 'System', text: data, color: 'yellow' }]);
    })
    .catch(error => {
      console.error("Transmission Error:", error);
      setMessages(prev => [...prev, { sender: 'System', text: 'ERROR: Connection to mainframe failed or unauthorized.', color: 'red' }]);
    });
  };

  return (
    <div className="login-box" style={{ width: '600px', textAlign: 'left' }}>
      
      {/* Header Section */}
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <h2>🤖 Sentinel Command Center</h2>
        <button 
          onClick={handleLogout} 
          style={{ padding: '8px 15px', fontSize: '12px', backgroundColor: '#d9534f', border: 'none', color: 'white', cursor: 'pointer', borderRadius: '4px', fontWeight: 'bold' }}
        >
          LOGOUT & DISCONNECT
        </button>
      </div>
      
      <p style={{ color: 'white', marginTop: '10px', marginBottom: '20px' }}>
        Welcome, Agent. Secure connection established.
      </p>
      
      {/* The Terminal Screen */}
      <div style={{ backgroundColor: '#000', padding: '20px', marginBottom: '20px', borderRadius: '4px', border: '1px solid #00ff88', height: '250px', overflowY: 'auto', fontFamily: 'monospace' }}>
        {messages.map((msg, index) => (
          <p key={index} style={{ color: msg.color, margin: '8px 0', lineHeight: '1.4' }}>
            <strong>[ {msg.sender} ] :</strong> {msg.text}
          </p>
        ))}
      </div>

      {/* The Command Input Field */}
      <form onSubmit={handleSendCommand} style={{ display: 'flex', gap: '10px' }}>
        <input 
          type="text" 
          placeholder="Enter command..." 
          value={input}
          onChange={(e) => setInput(e.target.value)}
          style={{ flex: 1, padding: '12px', backgroundColor: '#111', color: '#00ff88', border: '1px solid #444', fontFamily: 'monospace', borderRadius: '4px', outline: 'none' }}
        />
        <button type="submit" className="blue-btn" style={{ margin: 0, width: '130px', padding: '12px', cursor: 'pointer' }}>
          EXECUTE
        </button>
      </form>

    </div>
  );
}

export default Dashboard;