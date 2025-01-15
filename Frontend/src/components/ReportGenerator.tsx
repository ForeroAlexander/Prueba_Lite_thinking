import { FileDown, Mail, Download } from 'lucide-react';

interface ReportGeneratorProps {
  email: string;
  setEmail: (email: string) => void;
  isGeneratingReport: boolean;
  isDownloading: boolean;
  onGenerateReport: () => Promise<void>;
  onDownloadPdf: () => Promise<void>;
  reportMessage: string;
}

export function ReportGenerator({
  email,
  setEmail,
  isGeneratingReport,
  isDownloading,
  onGenerateReport,
  onDownloadPdf,
  reportMessage,
}: ReportGeneratorProps) {
  return (
    <div className="mb-6 p-6 bg-white shadow rounded-lg">
      <div className="flex items-center gap-2 mb-4">
        <FileDown className="h-5 w-5 text-indigo-600" />
        <h3 className="text-lg font-medium">Reporte de Inventario</h3>
      </div>
      <div className="flex gap-4">
        <div className="flex-1">
          <div className="relative">
            <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
              <Mail className="h-5 w-5 text-gray-400" />
            </div>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="Ingrese correo electrónico"
              className="block w-full pl-10 pr-3 py-2 border border-gray-300 rounded-md leading-5 bg-white placeholder-gray-500 focus:outline-none focus:placeholder-gray-400 focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
            />
          </div>
        </div>
        <button
          onClick={onGenerateReport}
          disabled={isGeneratingReport || !email}
          className="px-4 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50 disabled:cursor-not-allowed flex items-center gap-2"
        >
          <Mail className="h-4 w-4" />
          {isGeneratingReport ? 'Enviando...' : 'Enviar por Email'}
        </button>
        <button
          onClick={onDownloadPdf}
          disabled={isDownloading}
          className="px-4 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-green-600 hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 disabled:opacity-50 disabled:cursor-not-allowed flex items-center gap-2"
        >
          <Download className="h-4 w-4" />
          {isDownloading ? 'Descargando...' : 'Descargar PDF'}
        </button>
      </div>
      {reportMessage && (
        <p className={`mt-2 text-sm ${reportMessage.includes('Error') ? 'text-red-600' : 'text-green-600'}`}>
          {reportMessage}
        </p>
      )}
    </div>
  );
}