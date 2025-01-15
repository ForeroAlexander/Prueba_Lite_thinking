import React, { createContext, useContext, useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthState, User, LoginResponse } from '../types/auth';
import { api } from '../services/api';

interface AuthContextType extends AuthState {
  login: (email: string, password: string) => Promise<void>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const navigate = useNavigate();

  const [authState, setAuthState] = useState<AuthState>(() => {
    const token = localStorage.getItem('token');
    const userStr = localStorage.getItem('user');
    return {
      user: userStr ? JSON.parse(userStr) : null,
      isAuthenticated: !!token,
    };
  });

  const login = async (email: string, password: string) => {
    try {
      const response = await api.login(email, password) as LoginResponse;

      localStorage.setItem('token', response.token);
      // Generamos un ID único ya que no viene en la respuesta
      const userId = crypto.randomUUID();
      const user: User = {
        id: userId,
        email: response.email,
        role: response.role as 'ADMIN' | 'EXTERNAL',
      };
      localStorage.setItem('user', JSON.stringify(user));

      setAuthState({ user, isAuthenticated: true });
    } catch (error) {
      console.error('Error durante el login:', error);
      throw error;
    }
  };

  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    setAuthState({ user: null, isAuthenticated: false });
    navigate('/login');
  };

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      const userStr = localStorage.getItem('user');
      if (userStr) {
        const user = JSON.parse(userStr);
        setAuthState({ user, isAuthenticated: true });
      }
    }
  }, []);

  return (
    <AuthContext.Provider value={{ ...authState, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth debe ser usado dentro de un AuthProvider');
  }
  return context;
};
