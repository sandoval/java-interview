import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Lock API calls
export const lockAPI = {
  getAll: () => api.get('/locks'),
  getBySerial: (serial) => api.get(`/locks/${serial}`),
  create: (lock) => api.post('/locks', lock),
  update: (serial, lock) => api.put(`/locks/${serial}`, lock),
  delete: (serial) => api.delete(`/locks/${serial}`),
};

// Gateway API calls
export const gatewayAPI = {
  getAll: () => api.get('/gateways'),
  getBySerial: (serial) => api.get(`/gateways/${serial}`),
  create: (gateway) => api.post('/gateways', gateway),
  update: (serial, gateway) => api.put(`/gateways/${serial}`, gateway),
  delete: (serial) => api.delete(`/gateways/${serial}`),
};

// Lock-Gateway Link API calls
export const linkAPI = {
  getAll: () => api.get('/lock-gateway-links'),
  getByLockSerial: (lockSerial) => api.get(`/lock-gateway-links/lock/${lockSerial}`),
  getByGatewaySerial: (gatewaySerial) => api.get(`/lock-gateway-links/gateway/${gatewaySerial}`),
  getLink: (lockSerial, gatewaySerial) => api.get(`/lock-gateway-links/${lockSerial}/${gatewaySerial}`),
  create: (link) => api.post('/lock-gateway-links', link),
  update: (lockSerial, gatewaySerial, link) => api.put(`/lock-gateway-links/${lockSerial}/${gatewaySerial}`, link),
  delete: (lockSerial, gatewaySerial) => api.delete(`/lock-gateway-links/${lockSerial}/${gatewaySerial}`),
};

export default api; 