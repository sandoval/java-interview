import React from 'react';
import { Link } from 'react-router-dom';

function Home() {
  return (
    <div>
      <h1 className="page-title">Welcome to the Interview App</h1>
      <div className="card">
        <p style={{ textAlign: 'center', fontSize: '1.1rem', color: '#666', marginBottom: '2rem' }}>
          Manage your locks and gateways efficiently with our intuitive interface.
        </p>
        <div className="home-links">
          <Link to="/gateways" className="home-link">
            Manage Gateways
          </Link>
          <Link to="/locks" className="home-link">
            Manage Locks
          </Link>
        </div>
      </div>
    </div>
  );
}

export default Home; 