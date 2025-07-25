import React, { useState, useEffect } from 'react';
import { lockAPI } from '../services/api';

function Locks() {
  const [locks, setLocks] = useState([]);
  const [selectedLock, setSelectedLock] = useState(null);
  const [formData, setFormData] = useState({
    serial: '',
    name: '',
    macAddress: '',
    version: '',
    online: false
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchLocks();
  }, []);

  const fetchLocks = async () => {
    try {
      setLoading(true);
      const response = await lockAPI.getAll();
      setLocks(response.data);
      setError(null);
    } catch (err) {
      setError('Failed to fetch locks');
      console.error('Error fetching locks:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
  };

  const handleRowClick = (lock) => {
    setSelectedLock(lock);
    setFormData({
      serial: lock.serial,
      name: lock.name,
      macAddress: lock.macAddress,
      version: lock.version,
      online: lock.online
    });
  };

  const handleSave = async () => {
    try {
      if (selectedLock) {
        // Update existing lock
        await lockAPI.update(selectedLock.serial, formData);
      } else {
        // Create new lock
        await lockAPI.create(formData);
      }
      await fetchLocks();
      handleClear();
      setError(null);
    } catch (err) {
      setError(selectedLock ? 'Failed to update lock' : 'Failed to create lock');
      console.error('Error saving lock:', err);
    }
  };

  const handleDelete = async () => {
    if (selectedLock && window.confirm(`Are you sure you want to delete lock ${selectedLock.serial}?`)) {
      try {
        await lockAPI.delete(selectedLock.serial);
        await fetchLocks();
        handleClear();
        setError(null);
      } catch (err) {
        setError('Failed to delete lock');
        console.error('Error deleting lock:', err);
      }
    }
  };

  const handleClear = () => {
    setSelectedLock(null);
    setFormData({
      serial: '',
      name: '',
      macAddress: '',
      version: '',
      online: false
    });
  };

  if (loading) {
    return (
      <div>
        <h1 className="page-title">Locks</h1>
        <div className="card">
          <p>Loading locks...</p>
        </div>
      </div>
    );
  }

  return (
    <div>
      <h1 className="page-title">Locks</h1>
      
      {error && (
        <div className="card" style={{ backgroundColor: '#fee', borderLeft: '4px solid #e74c3c' }}>
          <p style={{ color: '#e74c3c', margin: 0 }}>{error}</p>
        </div>
      )}

      {/* Locks Table */}
      <div className="card">
        <h2>All Locks</h2>
        <table className="data-table">
          <thead>
            <tr>
              <th>Serial</th>
              <th>Name</th>
              <th>MAC Address</th>
              <th>Online</th>
              <th>Version</th>
            </tr>
          </thead>
          <tbody>
            {locks.map((lock) => (
              <tr
                key={lock.serial}
                onClick={() => handleRowClick(lock)}
                className={selectedLock?.serial === lock.serial ? 'selected' : ''}
              >
                <td>{lock.serial}</td>
                <td>{lock.name}</td>
                <td>{lock.macAddress}</td>
                <td>
                  <span className={lock.online ? 'status-online' : 'status-offline'}>
                    {lock.online ? 'Online' : 'Offline'}
                  </span>
                </td>
                <td>{lock.version}</td>
              </tr>
            ))}
            {locks.length === 0 && (
              <tr>
                <td colSpan="5" style={{ textAlign: 'center', color: '#666' }}>
                  No locks found
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      {/* Lock Form */}
      <div className="card">
        <h2>{selectedLock ? 'Edit Lock' : 'Add New Lock'}</h2>
        <div className="form-container">
          <div className="form-row">
            <div className="form-group">
              <label htmlFor="serial">Serial</label>
              <input
                type="text"
                id="serial"
                name="serial"
                value={formData.serial}
                onChange={handleInputChange}
                maxLength="16"
                disabled={selectedLock !== null}
                placeholder="Enter serial number"
              />
            </div>
            <div className="form-group">
              <label htmlFor="name">Name</label>
              <input
                type="text"
                id="name"
                name="name"
                value={formData.name}
                onChange={handleInputChange}
                maxLength="50"
                placeholder="Enter lock name"
              />
            </div>
          </div>
          <div className="form-row">
            <div className="form-group">
              <label htmlFor="macAddress">MAC Address</label>
              <input
                type="text"
                id="macAddress"
                name="macAddress"
                value={formData.macAddress}
                onChange={handleInputChange}
                placeholder="Enter MAC address"
              />
            </div>
            <div className="form-group">
              <label htmlFor="version">Version</label>
              <input
                type="text"
                id="version"
                name="version"
                value={formData.version}
                onChange={handleInputChange}
                maxLength="20"
                placeholder="Enter version"
              />
            </div>
          </div>
          {selectedLock && (
            <div className="form-row">
              <div className="form-group">
                <label>
                  <input
                    type="checkbox"
                    name="online"
                    checked={formData.online}
                    onChange={handleInputChange}
                  />
                  <span style={{ marginLeft: '0.5rem' }}>Online</span>
                </label>
              </div>
            </div>
          )}
          <div className="button-group">
            <button className="btn btn-primary" onClick={handleSave}>
              Save
            </button>
            {selectedLock && (
              <button className="btn btn-danger" onClick={handleDelete}>
                Delete
              </button>
            )}
            <button className="btn btn-secondary" onClick={handleClear}>
              Clear
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Locks; 