import { useState } from 'react';

export default function Login({ onLoginSuccess }) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const handleLogin = async (e) => {
    e.preventDefault();
    setError('');

    try {
      const response = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password }) 
      });

      const data = await response.json();

      if (response.ok) {
        // We store the "VIP Wristband" (JWT) in the browser
        localStorage.setItem('sentinel_token', data.token);
        onLoginSuccess(); 
      } else {
        setError(`[!] ${data.error || 'Access Denied'}`);
      }
    } catch (err) {
      setError('[!] CRITICAL: Auth server offline.');
    }
  };

  return (
    <div className="terminal-login" style={{ padding: '40px', textAlign: 'center' }}>
      <h2 style={{ color: '#00ff00' }}>=== SENTINEL AUTH GATEWAY ===</h2>
      <form onSubmit={handleLogin} style={{ display: 'inline-block', textAlign: 'left' }}>
        <div style={{ marginBottom: '15px' }}>
          <label>IDENTIFIER: </label>
          <input 
            type="text" 
            value={username} 
            onChange={(e) => setUsername(e.target.value)} 
            className="terminal-input"
            autoFocus 
          />
        </div>
        <div style={{ marginBottom: '15px' }}>
          <label>PASSPHRASE: </label>
          <input 
            type="password" 
            value={password} 
            onChange={(e) => setPassword(e.target.value)} 
            className="terminal-input"
          />
        </div>
        <button type="submit" className="terminal-button">INITIATE LINK</button>
      </form>
      {error && <p style={{ color: '#ff0000', marginTop: '20px' }}>{error}</p>}
    </div>
  );
}
