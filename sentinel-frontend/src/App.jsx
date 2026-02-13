import { useState, useEffect } from 'react'
import axios from 'axios'
import './App.css'

function App() {
  const [issues, setIssues] = useState([])
  const [title, setTitle] = useState("")
  const [description, setDescription] = useState("")

  // Load issues on startup 🔄
  const fetchIssues = () => {
    axios.get('http://localhost:8081/api/issues')
      .then(response => setIssues(response.data))
      .catch(err => console.error(err))
  }

  useEffect(() => {
    fetchIssues()
  }, [])

  // 👇 Function to handle the Form Submit
  const handleSubmit = (e) => {
    e.preventDefault() // Stop page refresh
    
    const newIssue = {
        title: title,
        description: description,
        severity: "HIGH",
        status: "OPEN"
    }

    axios.post('http://localhost:8081/api/issues', newIssue)
      .then(() => {
        fetchIssues() // Refresh list
        setTitle("")  // Clear form
        setDescription("")
      })
      .catch(err => alert("Error saving issue!"))
  }

  return (
    <div className="container" style={{maxWidth: '600px', margin: '0 auto'}}>
      <h1>🛡️ Sentinel AI Dashboard</h1>

      {/* 📝 THE NEW FORM */}
      <div className="card" style={{padding: '20px', marginBottom: '20px', border: '1px solid #444', borderRadius: '8px'}}>
        <h3>🚨 Report a New Bug</h3>
        <form onSubmit={handleSubmit} style={{display: 'flex', flexDirection: 'column', gap: '10px'}}>
          <input 
            type="text" 
            placeholder="Bug Title (e.g., App Crashed)" 
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            style={{padding: '10px'}}
            required
          />
          <textarea 
            placeholder="Paste the error log here..." 
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            style={{padding: '10px', height: '80px'}}
            required
          />
          <button type="submit" style={{backgroundColor: '#ff4444', color: 'white'}}>
            Report Bug
          </button>
        </form>
      </div>

      {/* 📋 THE LIST */}
      <div className="list">
        <h3>Current Issues ({issues.length})</h3>
        {issues.map(issue => (
          <div key={issue.id} style={{
              textAlign: 'left', 
              marginBottom: '10px', 
              padding: '15px', 
              backgroundColor: '#2a2a2a', 
              borderRadius: '8px'
            }}>
            <strong style={{color: '#ff6b6b'}}>{issue.title}</strong>
            <p style={{margin: '5px 0 0 0', fontSize: '0.9em', color: '#ccc'}}>
              {issue.description}
            </p>
          </div>
        ))}
      </div>
    </div>
  )
}

export default App