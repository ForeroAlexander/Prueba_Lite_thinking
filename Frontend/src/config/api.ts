export const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

export const handleResponse = async (response: Response) => {
  if (!response.ok) {
    let errorMessage = 'Ha ocurrido un error';
    try {
      const errorData = await response.json();
      console.error('Error response:', {
        status: response.status,
        statusText: response.statusText,
        data: errorData
      });
      errorMessage = errorData.message || errorData.error || errorMessage;
    } catch (e) {
      console.error('Error parsing error response:', e);
    }
    throw new Error(errorMessage);
  }
  return response.json();
};