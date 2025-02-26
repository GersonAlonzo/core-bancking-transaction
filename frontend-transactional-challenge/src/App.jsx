import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Layout from './components/Layout';
import ClientesPage from './pages/ClientesPage';
import ClienteFormPage from './pages/ClienteFormPage';
import CuentasPage from './pages/CuentasPage';
import CuentaFormPage from './pages/CuentaFormPage';
import MovimientosPage from './pages/MovimientosPage';
import MovimientoFormPage from './pages/MovimientoFormPage';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Layout />}>
          <Route index element={<Navigate to="/clientes" replace />} />
          <Route path="clientes" element={<ClientesPage />} />
          <Route path="clientes/nuevo" element={<ClienteFormPage />} />
          <Route path="clientes/editar/:id" element={<ClienteFormPage />} />
          <Route path="cuentas" element={<CuentasPage />} />
          <Route path="cuentas/nuevo" element={<CuentaFormPage />} />
          <Route path="cuentas/editar/:numeroCuenta" element={<CuentaFormPage />} />
          <Route path="movimientos" element={<MovimientosPage />} />
          <Route path="movimientos/nuevo" element={<MovimientoFormPage />} />
        </Route>
      </Routes>
    </Router>
  );
}

export default App;