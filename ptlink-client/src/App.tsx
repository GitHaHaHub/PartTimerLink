import './App.css'
import { redirect } from 'react-router'

function App() {

  return (
    <div>
      <div className="App">Home</div>
      <button type="button" onClick={() => redirect('/mypage')}>Sign Up</button>
    </div>
  )
}

export default App
