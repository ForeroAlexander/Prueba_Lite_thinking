import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { api } from '../services/api';
import { Package2, Pencil, Trash2 } from 'lucide-react';
import type { Product, NewProduct } from '../types/product';

const initialProduct: NewProduct = {
  codigo: '',
  nombre: '',
  caracteristicas: '',
  precio_usd: 0,
  precio_eur: 0,
  precio_cop: 0,
  empresa_nit: ''
};

export function Products() {
  const [product, setProduct] = useState<NewProduct>(initialProduct);
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const { user } = useAuth();
  const isAdmin = user?.role === 'ADMIN';

  useEffect(() => {
    loadProducts();
  }, []);

  const loadProducts = async () => {
    try {
      setLoading(true);
      const response = await api.getProducts();
      setProducts(response);
      setError(null);
    } catch (err) {
      setError('Error al cargar los productos');
      console.error('Error loading products:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await api.createProduct(product);
      setProduct(initialProduct);
      loadProducts();
    } catch (err) {
      console.error('Error creating product:', err);
      setError('Error al crear el producto');
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setProduct(prev => ({
      ...prev,
      [name]: name.includes('precio') ? parseFloat(value) : value
    }));
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center min-h-screen">
        <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600"></div>
      </div>
    );
  }

  return (
    <div className="max-w-4xl mx-auto py-8 px-4">
      <div className="flex items-center gap-2 mb-8">
        <Package2 className="h-8 w-8 text-indigo-600" />
        <h1 className="text-3xl font-bold text-gray-900">Gestión de Productos</h1>
      </div>
      
      {error && (
        <div className="bg-red-50 border-l-4 border-red-400 p-4 mb-8">
          <div className="flex">
            <div className="flex-shrink-0">
              <span className="text-red-400">⚠️</span>
            </div>
            <div className="ml-3">
              <p className="text-sm text-red-700">{error}</p>
            </div>
          </div>
        </div>
      )}
      
      {isAdmin && (
        <div className="bg-white shadow rounded-lg p-6 mb-8">
          <h2 className="text-xl font-semibold text-gray-800 mb-6">Registrar Nuevo Producto</h2>
          <form onSubmit={handleSubmit} className="space-y-6">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div>
                <label htmlFor="codigo" className="block text-sm font-medium text-gray-700 mb-1">
                  Código
                </label>
                <input
                  type="text"
                  id="codigo"
                  name="codigo"
                  required
                  value={product.codigo}
                  onChange={handleChange}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                />
              </div>
              
              <div>
                <label htmlFor="nombre" className="block text-sm font-medium text-gray-700 mb-1">
                  Nombre del Producto
                </label>
                <input
                  type="text"
                  id="nombre"
                  name="nombre"
                  required
                  value={product.nombre}
                  onChange={handleChange}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                />
              </div>
              
              <div className="md:col-span-2">
                <label htmlFor="caracteristicas" className="block text-sm font-medium text-gray-700 mb-1">
                  Características
                </label>
                <textarea
                  id="caracteristicas"
                  name="caracteristicas"
                  value={product.caracteristicas}
                  onChange={handleChange}
                  rows={3}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                />
              </div>
              
              <div>
                <label htmlFor="precio_usd" className="block text-sm font-medium text-gray-700 mb-1">
                  Precio USD
                </label>
                <input
                  type="number"
                  id="precio_usd"
                  name="precio_usd"
                  required
                  min="0"
                  step="0.01"
                  value={product.precio_usd}
                  onChange={handleChange}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                />
              </div>

              <div>
                <label htmlFor="precio_eur" className="block text-sm font-medium text-gray-700 mb-1">
                  Precio EUR
                </label>
                <input
                  type="number"
                  id="precio_eur"
                  name="precio_eur"
                  required
                  min="0"
                  step="0.01"
                  value={product.precio_eur}
                  onChange={handleChange}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                />
              </div>

              <div>
                <label htmlFor="precio_cop" className="block text-sm font-medium text-gray-700 mb-1">
                  Precio COP
                </label>
                <input
                  type="number"
                  id="precio_cop"
                  name="precio_cop"
                  required
                  min="0"
                  step="1"
                  value={product.precio_cop}
                  onChange={handleChange}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                />
              </div>

              <div>
                <label htmlFor="empresa_nit" className="block text-sm font-medium text-gray-700 mb-1">
                  NIT de la Empresa
                </label>
                <input
                  type="text"
                  id="empresa_nit"
                  name="empresa_nit"
                  required
                  value={product.empresa_nit}
                  onChange={handleChange}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
                />
              </div>
            </div>

            <div className="flex justify-end">
              <button
                type="submit"
                className="px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
              >
                Guardar Producto
              </button>
            </div>
          </form>
        </div>
      )}

      <div className="bg-white shadow rounded-lg p-6">
        <h2 className="text-xl font-semibold text-gray-800 mb-6">Productos Registrados</h2>
        {products.length > 0 ? (
          <div className="overflow-x-auto">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Código</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Nombre</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Características</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Precio USD</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Precio EUR</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Precio COP</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Empresa</th>
                  {isAdmin && (
                    <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Acciones</th>
                  )}
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {products.map((product) => (
                  <tr key={product.codigo}>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{product.codigo}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{product.nombre}</td>
                    <td className="px-6 py-4 text-sm text-gray-900">{product.caracteristicas}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">${product.precio_usd}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">€{product.precio_eur}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">COP {product.precio_cop}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{product.empresa.nombre}</td>
                    {isAdmin && (
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                        <div className="flex space-x-2">
                          <button
                            className="text-indigo-600 hover:text-indigo-900"
                            title="Editar"
                          >
                            <Pencil className="h-5 w-5" />
                          </button>
                          <button
                            className="text-red-600 hover:text-red-900"
                            title="Eliminar"
                          >
                            <Trash2 className="h-5 w-5" />
                          </button>
                        </div>
                      </td>
                    )}
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        ) : (
          <p className="text-gray-500 text-center py-4">No hay productos registrados.</p>
        )}
      </div>
    </div>
  );
}