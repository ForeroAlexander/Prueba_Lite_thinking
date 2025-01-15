export interface Product {
  codigo: string;
  nombre: string;
  caracteristicas: string;
  precio_usd: number;
  precio_eur: number;
  precio_cop: number;
  empresa: {
    nit: string;
    nombre: string;
  };
}

export interface NewProduct {
  codigo: string;
  nombre: string;
  caracteristicas: string;
  precio_usd: number;
  precio_eur: number;
  precio_cop: number;
  empresa_nit: string;
}