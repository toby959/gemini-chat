import { useState } from 'react'
import './App.css'
import ChatInput from './components/ChatInput'
import ChatResponse from './components/ChatResponse'
import { fetchChatResponse } from './services/api';

function App() {
  const [response, setResponse] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleQuestionSubmit = async (question) => {
    setLoading(true);
    setResponse(null);
    try {
      const apiResponse = await fetchChatResponse(question);
      setResponse(apiResponse);
    } catch (error) {
      alert("Failed to get response")
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className='App'>
      <header className='bg-success text-white text-center py-4'>
        <h1>Gemini-1.5-flash ChatBot</h1>
      </header>
      {/* INPUT */}
      <ChatInput onSubmit={handleQuestionSubmit} />
      {loading && <h3>Loading...</h3>}

      {/* RESPONSE */}
      <ChatResponse response={response} />
    </div>
  )
}
export default App
