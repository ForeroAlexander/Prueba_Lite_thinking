export interface User {
  id: string;
  email: string;
  role: 'ADMIN' | 'EXTERNAL';
}

export interface AuthState {
  user: User | null;
  isAuthenticated: boolean;
}

export interface LoginResponse {
  token: string;
  email: string;
  role: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}