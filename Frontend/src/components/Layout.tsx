import  { useState } from 'react';
import { Outlet, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { Building2, Package, ClipboardList, LogOut, Menu, X } from 'lucide-react';

export function Layout() {
  const { user, logout } = useAuth();
  const [isOpen, setIsOpen] = useState(false);

  const toggleMenu = () => {
    setIsOpen(!isOpen);
  };

  return (
    <div className="min-h-screen bg-gray-100">
      <nav className="bg-white shadow-lg">
        <div className="max-w-7xl mx-auto px-4">
          <div className="flex justify-between h-16">
            <div className="flex">
              <Link to="/" className="flex items-center px-2 py-2 text-gray-700 hover:text-gray-900">
                <Building2 className="h-6 w-6 mr-2" />
                <span className="font-semibold">Sistema Empresarial</span>
              </Link>
            </div>

            {/* Botón de menú móvil */}
            <div className="flex items-center md:hidden">
              <button
                onClick={toggleMenu}
                className="inline-flex items-center justify-center p-2 rounded-md text-gray-700 hover:text-gray-900 hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-inset focus:ring-indigo-500"
              >
                {isOpen ? (
                  <X className="h-6 w-6" />
                ) : (
                  <Menu className="h-6 w-6" />
                )}
              </button>
            </div>
            
            {/* Menú de escritorio */}
            <div className="hidden md:flex items-center space-x-4">
              {user ? (
                <>
                  <Link to="/empresas" className="flex items-center px-3 py-2 text-sm text-gray-700 hover:text-gray-900">
                    <Building2 className="h-5 w-5 mr-1" />
                    Empresas
                  </Link>
                  <Link to="/productos" className="flex items-center px-3 py-2 text-sm text-gray-700 hover:text-gray-900">
                    <Package className="h-5 w-5 mr-1" />
                    Productos
                  </Link>
                  <Link to="/inventario" className="flex items-center px-3 py-2 text-sm text-gray-700 hover:text-gray-900">
                    <ClipboardList className="h-5 w-5 mr-1" />
                    Inventario
                  </Link>
                  <button
                    onClick={logout}
                    className="flex items-center px-3 py-2 text-sm text-red-600 hover:text-red-800"
                  >
                    <LogOut className="h-5 w-5 mr-1" />
                    Salir
                  </button>
                </>
              ) : (
                <Link
                  to="/login"
                  className="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700"
                >
                  Iniciar Sesión
                </Link>
              )}
            </div>
          </div>

          {/* Menú móvil */}
          <div className={`${isOpen ? 'block' : 'hidden'} md:hidden pb-3`}>
            {user ? (
              <div className="space-y-1">
                <Link
                  to="/empresas"
                  className="flex items-center px-3 py-2 text-base text-gray-700 hover:text-gray-900 hover:bg-gray-50 rounded-md"
                  onClick={() => setIsOpen(false)}
                >
                  <Building2 className="h-5 w-5 mr-2" />
                  Empresas
                </Link>
                <Link
                  to="/productos"
                  className="flex items-center px-3 py-2 text-base text-gray-700 hover:text-gray-900 hover:bg-gray-50 rounded-md"
                  onClick={() => setIsOpen(false)}
                >
                  <Package className="h-5 w-5 mr-2" />
                  Productos
                </Link>
                <Link
                  to="/inventario"
                  className="flex items-center px-3 py-2 text-base text-gray-700 hover:text-gray-900 hover:bg-gray-50 rounded-md"
                  onClick={() => setIsOpen(false)}
                >
                  <ClipboardList className="h-5 w-5 mr-2" />
                  Inventario
                </Link>
                <button
                  onClick={() => {
                    logout();
                    setIsOpen(false);
                  }}
                  className="flex w-full items-center px-3 py-2 text-base text-red-600 hover:text-red-800 hover:bg-gray-50 rounded-md"
                >
                  <LogOut className="h-5 w-5 mr-2" />
                  Salir
                </button>
              </div>
            ) : (
              <div className="px-3 py-2">
                <Link
                  to="/login"
                  className="block w-full text-center px-4 py-2 text-base font-medium text-white bg-indigo-600 hover:bg-indigo-700 rounded-md"
                  onClick={() => setIsOpen(false)}
                >
                  Iniciar Sesión
                </Link>
              </div>
            )}
          </div>
        </div>
      </nav>

      <main className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        <Outlet />
      </main>
    </div>
  );
}