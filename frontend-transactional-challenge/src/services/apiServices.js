import axios from 'axios';


const apiClient = axios.create({
  baseURL: '/api', 
  headers: {
    'Content-Type': 'application/json',
  },
});


apiClient.interceptors.request.use(
  (config) => {
    const auth = btoa('admin:admin');
    config.headers.Authorization = `Basic ${auth}`;
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Servicios para clientes
const clienteService = {
  getAll: () => apiClient.get('/clientes'),
  getById: (id) => apiClient.get(`/clientes/${id}`),
  create: (data) => apiClient.post('/clientes/registrar', data),
  update: (data) => apiClient.put('/clientes/actualizar', data),
  delete: (id) => apiClient.delete(`/clientes/${id}`),
};

// Servicios para cuentas
const cuentaService = {
  getAll: () => apiClient.get('/cuentas'),
  getByNumeroCuenta: (numeroCuenta) => apiClient.get(`/cuentas/${numeroCuenta}`),
  getByClienteId: (clienteId) => apiClient.get(`/cuentas/cliente/${clienteId}`),
  create: (data) => apiClient.post('/cuentas/registrar', data),
  delete: (numeroCuenta) => apiClient.delete(`/cuentas/${numeroCuenta}`),
};

// Servicios para movimientos
const movimientoService = {
  getAll: () => apiClient.get('/movimientos'),
  getByCuenta: (numeroCuenta) => apiClient.get(`/movimientos/${numeroCuenta}`),
  create: (data) => apiClient.post('/movimientos/registrar', data),
};
export { clienteService, cuentaService, movimientoService };