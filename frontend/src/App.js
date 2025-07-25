import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import Home from './components/Home';
import Locks from './components/Locks';
import Gateways from './components/Gateways';
import './App.css';

function App() {
  return (
    <Router>
      <div className="App">
        <nav className="navbar">
          <div className="nav-container">
            <Link to="/" className="nav-title">Interview App</Link>
            <div className="nav-links">
              <Link to="/" className="nav-link">Home</Link>
              <Link to="/locks" className="nav-link">Locks</Link>
              <Link to="/gateways" className="nav-link">Gateways</Link>
            </div>
          </div>
        </nav>
        
        <main className="main-content">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/locks" element={<Locks />} />
            <Route path="/gateways" element={<Gateways />} />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;
