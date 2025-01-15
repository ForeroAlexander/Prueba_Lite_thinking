import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import { Layout } from './components/Layout';
import { Login } from './pages/Login';
import { Companies } from './pages/Companies';
import { Products } from './pages/Products';
import { Inventory } from './pages/Inventory';

export default function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Routes>
          <Route path="/" element={<Layout />}>
            <Route index element={<div>Bienvenido al Sistema Empresarial</div>} />
            <Route path="login" element={<Login />} />
            <Route path="empresas" element={<Companies />} />
            <Route path="productos" element={<Products />} />
            <Route path="inventario" element={<Inventory />} />
            <Route path="*" element={<Navigate to="/login" replace />} />
          </Route>
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
}