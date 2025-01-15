import  { useState, useEffect } from 'react';
import { api } from '../services/api';
import { InventoryTable } from '../components/InventoryTable';
import { ReportGenerator } from '../components/ReportGenerator';
import type { InventoryItem } from '../types/inventory';

export function Inventory() {
  const [inventoryData, setInventoryData] = useState<InventoryItem[]>([]);
  const [email, setEmail] = useState('');
  const [isGeneratingReport, setIsGeneratingReport] = useState(false);
  const [isDownloading, setIsDownloading] = useState(false);
  const [reportMessage, setReportMessage] = useState('');

  useEffect(() => {
    const loadInventoryData = async () => {
      try {
        const data = await api.getProducts();
        const inventoryItems = data.map((product: any) => ({
          productCode: product.codigo,
          quantity: product.stock || 0,
          location: product.ubicacion || 'Sin ubicación',
          lastUpdated: new Date().toISOString().split('T')[0],
          minStock: product.stockMinimo || 0,
          maxStock: product.stockMaximo || 0,
        }));
        setInventoryData(inventoryItems);
      } catch (error) {
        console.error('Error al cargar el inventario:', error);
      }
    };

    loadInventoryData();
  }, []);

  const handleGenerateReport = async () => {
    try {
      setIsGeneratingReport(true);
      await api.generateInventoryReport(email);
      setReportMessage('Reporte generado y enviado correctamente');
      setEmail('');
    } catch (error) {
      setReportMessage('Error al generar el reporte: ' + (error as Error).message);
    } finally {
      setIsGeneratingReport(false);
    }
  };

  const handleDownloadPdf = async () => {
    try {
      setIsDownloading(true);
      await api.downloadInventoryReport();
      setReportMessage('Reporte descargado correctamente');
    } catch (error) {
      setReportMessage('Error al descargar el reporte: ' + (error as Error).message);
    } finally {
      setIsDownloading(false);
    }
  };

  return (
    <div className="max-w-7xl mx-auto py-8 px-4 sm:px-6 lg:px-8">
      <div className="flex items-center gap-3 mb-8">
        <h1 className="text-3xl font-bold text-gray-900">Control de Inventario</h1>
      </div>

      <ReportGenerator
        email={email}
        setEmail={setEmail}
        isGeneratingReport={isGeneratingReport}
        isDownloading={isDownloading}
        onGenerateReport={handleGenerateReport}
        onDownloadPdf={handleDownloadPdf}
        reportMessage={reportMessage}
      />

      <InventoryTable inventoryData={inventoryData} />
    </div>
  );
}