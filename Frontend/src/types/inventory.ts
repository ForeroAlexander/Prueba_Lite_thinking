export interface InventoryItem {
    productCode: string;
    quantity: number;
    location: string;
    lastUpdated: string;
    minStock: number;
    maxStock: number;
  }