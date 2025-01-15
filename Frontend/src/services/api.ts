import { API_BASE_URL } from '../config/api';
import type { LoginResponse, LoginRequest } from '../types/auth';
import type { Product, NewProduct } from '../types/product';

const getAuthHeader = () => {
  const token = localStorage.getItem('token');
  return token ? { Authorization: `Bearer ${token}` } : undefined;
};

const handleApiResponse = async (response: Response) => {
  if (!response.ok) {
    const text = await response.text();
    throw new Error(`Error del servidor: ${text || response.statusText}`);
  }

  const contentType = response.headers.get('content-type');
  
  if (contentType?.includes('application/pdf')) {
    return response.blob();
  }

  const text = await response.text();
  if (!text) return null;
  
  return contentType?.includes('application/json') ? JSON.parse(text) : text;
};

export const api = {
  login: async (email: string, password: string): Promise<LoginResponse> => {
    const loginRequest: LoginRequest = { email, password };
    
    const response = await fetch(`${API_BASE_URL}/auth/login`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(loginRequest),
    });
    
    return handleApiResponse(response);
  },

  getCompanies: async () => {
    const headers: HeadersInit = {
      'Accept': 'application/json'
    };
    
    const authHeader = getAuthHeader();
    if (authHeader) {
      Object.assign(headers, authHeader);
    }

    const response = await fetch(`${API_BASE_URL}/empresas`, { headers });
    return handleApiResponse(response);
  },

  createCompany: async (company: any) => {
    const headers: HeadersInit = {
      'Content-Type': 'application/json'
    };
    
    const authHeader = getAuthHeader();
    if (authHeader) {
      Object.assign(headers, authHeader);
    }

    const response = await fetch(`${API_BASE_URL}/empresas`, {
      method: 'POST',
      headers,
      body: JSON.stringify(company),
    });
    
    return handleApiResponse(response);
  },

  getProducts: async (): Promise<Product[]> => {
    const headers: HeadersInit = {
      'Accept': 'application/json'
    };
    
    const authHeader = getAuthHeader();
    if (authHeader) {
      Object.assign(headers, authHeader);
    }

    const response = await fetch(`${API_BASE_URL}/productos`, { headers });
    return handleApiResponse(response);
  },

  createProduct: async (product: NewProduct) => {
    const headers: HeadersInit = {
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    };
    
    const authHeader = getAuthHeader();
    if (authHeader) {
      Object.assign(headers, authHeader);
    }

    const response = await fetch(`${API_BASE_URL}/productos`, {
      method: 'POST',
      headers,
      body: JSON.stringify(product),
    });
    
    return handleApiResponse(response);
  },

  generateInventoryReport: async (email: string) => {
    const headers: HeadersInit = {
      'Content-Type': 'application/json'
    };
    
    const authHeader = getAuthHeader();
    if (authHeader) {
      Object.assign(headers, authHeader);
    }

    const response = await fetch(`${API_BASE_URL}/inventario/reporte`, {
      method: 'POST',
      headers,
      body: JSON.stringify({ emailDestino: email, formatoReporte: 'PDF' })
    });
    
    return handleApiResponse(response);
  },

  downloadInventoryReport: async () => {
    const headers: HeadersInit = {
      'Accept': 'application/pdf'
    };
    
    const authHeader = getAuthHeader();
    if (authHeader) {
      Object.assign(headers, authHeader);
    }

    const response = await fetch(`${API_BASE_URL}/inventario/download`, {
      headers
    });
    
    if (!response.ok) {
      const text = await response.text();
      throw new Error(`Error al descargar el PDF: ${text || response.statusText}`);
    }
    
    const blob = await response.blob();
    const contentDisposition = response.headers.get('content-disposition');
    let filename = 'inventario.pdf';
    
    if (contentDisposition) {
      const filenameMatch = contentDisposition.match(/filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/);
      if (filenameMatch && filenameMatch[1]) {
        filename = filenameMatch[1].replace(/['"]/g, '');
      }
    }
    
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = filename;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    window.URL.revokeObjectURL(url);
  }
};
