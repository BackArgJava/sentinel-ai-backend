import { useState } from 'react'
import './App.css'

function App() {
  // 1. STATE: The memory of our application
  const [errorInput, setErrorInput] = useState(''); // Remembers what the user types
  const [aiResponse, setAiResponse] = useState(''); // Remembers what Gemini answers
  const [isLoading, setIsLoading] = useState(false); // Remembers if we are waiting

  // 2. THE BRIDGE: This function talks to your Spring Boot Server
  const askSentinel = async () => {
    if (!errorInput) return; // Don't do anything if the box is empty
    
    setIsLoading(true);
    setAiResponse(''); // Clear the old answer

    try {
      const response = await fetch('http://localhost:8081/api/issues/analyze-error', {
        method: 'POST',
        headers: {
          'Content-Type': 'text/plain',
        },
        body: errorInput,
      });

      const text = await response.text();
      setAiResponse(text); // Save Gemini's answer to our State
      
    } catch (error) {
      setAiResponse("Connection failed! Is the Spring Boot server running?");
    } finally {
      setIsLoading(false); // Stop the loading animation
    }
  };

  // 3. THE FACE: What you actually see on the screen
  return (
    <div>
      <h1>Sentinel AI Dashboard 🛡️</h1>
      <p>Paste your Java error below and let the AI fix it.</p>
      
      {/* The Input Box */}
      <textarea 
        rows="6" 
        cols="60" 
        placeholder='e.g., java.lang.NullPointerException...'
        value={errorInput}
        onChange={(e) => setErrorInput(e.target.value)}
      />
      
      <br/><br/>
      
      {/* The Submit Button */}
      <button onClick={askSentinel} disabled={isLoading}>
        {isLoading ? 'Analyzing with Gemini...' : 'Analyze Error'}
      </button>

      {/* The AI Response Box (Only shows up if there is an answer) */}
      {aiResponse && (
        <div style={{ marginTop: '20px', padding: '15px', border: '1px solid #444', borderRadius: '8px', backgroundColor: '#1a1a1a', color: '#fff', textAlign: 'left' }}>
          <h3>AI Diagnosis:</h3>
          <p style={{ whiteSpace: 'pre-wrap' }}>{aiResponse}</p>
        </div>
      )}
    </div>
  )
}

export default App