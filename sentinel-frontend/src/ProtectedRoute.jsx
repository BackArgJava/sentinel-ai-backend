import { Navigate } from 'react-router-dom';

function ProtectedRoute({ children }) {
  // 1. The Bouncer checks the browser's secret vault for the VIP wristband
  const token = localStorage.getItem('sentinelToken');

  // 2. If the user does NOT have a token...
  if (!token) {
    // 3. Kick them immediately back to the Lobby ("/")
    return <Navigate to="/" replace />;
  }

  // 4. If they DO have a token, step aside and let them in (render the component)
  return children;
}

export default ProtectedRoute;