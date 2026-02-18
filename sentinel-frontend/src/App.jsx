import { useState } from 'react'
import './App.css'

function App() {
  // 1. React "State" variables to remember our data
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const [messageColor, setMessageColor] = useState('white');
  const [token, setToken] = useState('');
  const [secureData, setSecureData] = useState(null);

  // 2. The Login Function
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
        setMessage("ACCESS GRANTED!");
        setMessageColor("#00ff88");
        setToken(res.body.token); // Save the VIP wristband in React State!
      } else {
        setMessage("ACCESS DENIED!");
        setMessageColor("red");
      }
    })
    .catch(error => {
      console.error("Error:", error);
      setMessage("Network Blocked!");
      setMessageColor("red");
    });
  };

  // 3. The Fetch Secure Data Function
  const fetchSecureData = () => {
    fetch('http://localhost:8081/api/users', {
      method: 'GET',
      headers: { 
        'Authorization': 'Bearer ' + token // Show the Bouncer the token
      }
    })
    .then(response => response.json())
    .then(data => {
      setSecureData(JSON.stringify(data, null, 2));
    })
    .catch(error => console.error("Error fetching data:", error));
  };

  // 4. The Skeleton (JSX - HTML mixed with JavaScript)
  return (
    <div className="login-box">
      <h2>Sentinel AI VIP</h2>
      
      <input 
        type="text" 
        placeholder="Enter Username" 
        value={username}
        onChange={(e) => setUsername(e.target.value)} 
      />
      
      <input 
        type="password" 
        placeholder="Enter Password" 
        value={password}
        onChange={(e) => setPassword(e.target.value)} 
      />
      
      <button onClick={handleLogin}>1. ACCESS TERMINAL</button>
      
      <p style={{ color: messageColor, fontWeight: 'bold', marginTop: '15px' }}>
        {message}
      </p>

      {/* Only show this button IF we have a token */}
      {token && (
        <>
          <button className="blue-btn" onClick={fetchSecureData}>
            2. FETCH SECURE USERS
          </button>
          
          {/* Only show the black data box IF we have secureData */}
          {secureData && (
            <pre className="secure-data-box">{secureData}</pre>
          )}
        </>
      )}
    </div>
  )
}

export default App