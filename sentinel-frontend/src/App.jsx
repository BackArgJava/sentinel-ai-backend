import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Login from './Login';
import Dashboard from './Dashboard';
import ProtectedRoute from './ProtectedRoute'; // Import the new Guard
import './App.css'; 

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Route 1: The Lobby (Public) */}
        <Route path="/" element={<Login />} />
        
        {/* Route 2: The Command Center (Locked!) */}
        <Route 
          path="/dashboard" 
          element={
            <ProtectedRoute>
              <Dashboard />
            </ProtectedRoute>
          } 
        />
      </Routes>
    </BrowserRouter>
  );
}

export default App;