import React, { useState, useEffect } from 'react';
import { gatewayAPI } from '../services/api';

function Gateways() {
  const [gateways, setGateways] = useState([]);
  const [selectedGateway, setSelectedGateway] = useState(null);
  const [formData, setFormData] = useState({
    serial: '',
    macAddress: '',
    version: '',
    online: false
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchGateways();
  }, []);

  const fetchGateways = async () => {
    try {
      setLoading(true);
      const response = await gatewayAPI.getAll();
      setGateways(response.data);
      setError(null);
    } catch (err) {
      setError('Failed to fetch gateways');
      console.error('Error fetching gateways:', err);
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

  const handleRowClick = (gateway) => {
    setSelectedGateway(gateway);
    setFormData({
      serial: gateway.serial,
      macAddress: gateway.macAddress,
      version: gateway.version,
      online: gateway.online
    });
  };

  const handleSave = async () => {
    try {
      if (selectedGateway) {
        // Update existing gateway
        await gatewayAPI.update(selectedGateway.serial, formData);
      } else {
        // Create new gateway
        await gatewayAPI.create(formData);
      }
      await fetchGateways();
      handleClear();
      setError(null);
    } catch (err) {
      setError(selectedGateway ? 'Failed to update gateway' : 'Failed to create gateway');
      console.error('Error saving gateway:', err);
    }
  };

  const handleDelete = async () => {
    if (selectedGateway && window.confirm(`Are you sure you want to delete gateway ${selectedGateway.serial}?`)) {
      try {
        await gatewayAPI.delete(selectedGateway.serial);
        await fetchGateways();
        handleClear();
        setError(null);
      } catch (err) {
        setError('Failed to delete gateway');
        console.error('Error deleting gateway:', err);
      }
    }
  };

  const handleClear = () => {
    setSelectedGateway(null);
    setFormData({
      serial: '',
      macAddress: '',
      version: '',
      online: false
    });
  };

  if (loading) {
    return (
      <div>
        <h1 className="page-title">Gateways</h1>
        <div className="card">
          <p>Loading gateways...</p>
        </div>
      </div>
    );
  }

  return (
    <div>
      <h1 className="page-title">Gateways</h1>
      
      {error && (
        <div className="card" style={{ backgroundColor: '#fee', borderLeft: '4px solid #e74c3c' }}>
          <p style={{ color: '#e74c3c', margin: 0 }}>{error}</p>
        </div>
      )}

      {/* Gateways Table */}
      <div className="card">
        <h2>All Gateways</h2>
        <table className="data-table">
          <thead>
            <tr>
              <th>Serial</th>
              <th>MAC Address</th>
              <th>Online</th>
              <th>Version</th>
            </tr>
          </thead>
          <tbody>
            {gateways.map((gateway) => (
              <tr
                key={gateway.serial}
                onClick={() => handleRowClick(gateway)}
                className={selectedGateway?.serial === gateway.serial ? 'selected' : ''}
              >
                <td>{gateway.serial}</td>
                <td>{gateway.macAddress}</td>
                <td>
                  <span className={gateway.online ? 'status-online' : 'status-offline'}>
                    {gateway.online ? 'Online' : 'Offline'}
                  </span>
                </td>
                <td>{gateway.version}</td>
              </tr>
            ))}
            {gateways.length === 0 && (
              <tr>
                <td colSpan="4" style={{ textAlign: 'center', color: '#666' }}>
                  No gateways found
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      {/* Gateway Form */}
      <div className="card">
        <h2>{selectedGateway ? 'Edit Gateway' : 'Add New Gateway'}</h2>
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
                disabled={selectedGateway !== null}
                placeholder="Enter serial number"
              />
            </div>
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
          </div>
          <div className="form-row">
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
            {selectedGateway && (
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
            )}
          </div>
          <div className="button-group">
            <button className="btn btn-primary" onClick={handleSave}>
              Save
            </button>
            {selectedGateway && (
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

export default Gateways; 