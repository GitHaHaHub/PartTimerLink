import './App.css'
import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router'
import api from './api';

function App() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleSubmit = (event: React.FormEvent) => {
    event.preventDefault();

    console.log('Username:', username);
    console.log('Password:', password);

    // On successful login, redirect to the home page
    // Add your authentication logic here
    api.post('/generateToken', {
      username: username,
      password: password,
    },
    { withCredentials: true }
    ).then((response) => {
      console.log(response.data);
      axios.defaults.headers.common['Authorization'] = `${response.data.Authorization}`;
      console.log(axios.defaults.headers.common['Authorization']);
      navigate('/');
    }
    ).catch((error) => {
      console.error(error);
      alert('Invalid credentials');
    }
    );
  };

  return (
    <div className="login-wrapper">
      <h1 className="title">PartTimerLink</h1>
      <p className="subtitle">Your Part-time Job Partner</p>

      <div className="login-container">
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="username">Username</label>
            <input
              type="text"
              id="username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              placeholder="johndoe123"
              required
            />
          </div>

          <div className="form-group">
            <label htmlFor="password">Password</label>
            <input
              type="password"
              id="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="********"
              required
            />
          </div>

          <button type="submit" className="sign-in-btn">Sign In</button>
        </form>

        <button type="button" className="sign-up-link" onClick={() => navigate('/signup')}>
          Don’t have an account?
        </button>
      </div>
    </div>
  );
};

export default App
