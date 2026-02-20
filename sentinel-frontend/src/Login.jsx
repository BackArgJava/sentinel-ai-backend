import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

function Login() {
  const navigate = useNavigate(); // This is our GPS teleporter!
  
  const [isLoginMode, setIsLoginMode] = useState(true);
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState(''); 
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const [messageColor, setMessageColor] = useState('white');

  const handleLogin = () => {
    setMessage("Connecting to Bouncer...");
    setMessageColor("yellow");

    fetch('http://localhost:8081/api/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username: username, password: password })
    })
    .then(response => response.json().then(data => ({ status: response.status, body: data })))
    .then(res => {
      if (res.status === 200) {
        // SUCCESS! Save the token
        localStorage.setItem('sentinelToken', res.body.token);
        
        // TELEPORT THE USER TO THE DASHBOARD!
        navigate('/dashboard'); 
      } else {
        setMessage("ACCESS DENIED! Wrong credentials.");
        setMessageColor("red");
      }
    })
    .catch(error => {
      console.error("Error:", error);
      setMessage("Network Blocked!");
      setMessageColor("red");
    });
  };

  const handleRegister = () => {
    setMessage("Creating new agent...");
    setMessageColor("yellow");

    fetch('http://localhost:8081/api/users', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username: username, email: email, password: password })
    })
    .then(async response => {
      if (response.status === 200 || response.status === 201) {
        setMessage("REGISTRATION SUCCESSFUL! You can now log in.");
        setMessageColor("#00ff88");
        setPassword('');
        setIsLoginMode(true); 
      } else {
        setMessage("REGISTRATION FAILED.");
        setMessageColor("red");
      }
    })
    .catch(error => {
      console.error("Error:", error);
      setMessage("Network Error!");
      setMessageColor("red");
    });
  };

  return (
    <div className="login-box">
      <h2>{isLoginMode ? "Sentinel AI VIP" : "Agent Recruitment"}</h2>
      
      <input type="text" placeholder="Enter Username" value={username} onChange={(e) => setUsername(e.target.value)} />
      {!isLoginMode && ( <input type="email" placeholder="Enter Email" value={email} onChange={(e) => setEmail(e.target.value)} /> )}
      <input type="password" placeholder="Enter Password" value={password} onChange={(e) => setPassword(e.target.value)} />
      
      {isLoginMode ? ( <button onClick={handleLogin}>ACCESS TERMINAL</button> ) : ( <button onClick={handleRegister}>CREATE ACCOUNT</button> )}
      
      <p style={{ color: messageColor, fontWeight: 'bold', marginTop: '15px' }}>{message}</p>

      <p style={{ fontSize: '12px', cursor: 'pointer', color: '#0088ff', textDecoration: 'underline' }} onClick={() => { setIsLoginMode(!isLoginMode); setMessage(''); }}>
        {isLoginMode ? "Need an account? Register here." : "Already have an account? Log in."}
      </p>
    </div>
  );
}

export default Login;