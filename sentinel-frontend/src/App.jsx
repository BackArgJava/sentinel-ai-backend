import { useState, useEffect } from 'react';
import Login from './components/Login';
import Terminal from './components/Terminal'; // Your existing terminal component

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  // Check if user is already logged in when the page loads
  useEffect(() => {
    const token = localStorage.getItem('sentinel_token');
    if (token) setIsAuthenticated(true);
  }, []);

  const logout = () => {
    localStorage.removeItem('sentinel_token');
    setIsAuthenticated(false);
  };

  return (
    <div className="App">
      {!isAuthenticated ? (
        <Login onLoginSuccess={() => setIsAuthenticated(true)} />
      ) : (
        <>
          <button onClick={logout} style={{ float: 'right', margin: '10px' }}>LOGOUT</button>
          <Terminal />
        </>
      )}
    </div>
  );
}

export default App;