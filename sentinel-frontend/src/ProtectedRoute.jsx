import { Navigate } from 'react-router-dom';

function ProtectedRoute({ children }) {
  // The guard checks the browser's secret vault for the token (both localStorage and sessionStorage)
  const hasToken = localStorage.getItem('sentinelToken') || sessionStorage.getItem('sentinelToken');

  if (!hasToken) {
    // No token? ACCESS DENIED. Teleport back to the Lobby.
    return <Navigate to="/" />;
  }

  // Token found? Let them pass!
  return children;
}

export default ProtectedRoute;