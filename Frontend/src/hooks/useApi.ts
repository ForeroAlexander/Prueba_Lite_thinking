import { useState } from 'react';

export function useApi() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleRequest = async <T>(request: () => Promise<T>): Promise<T | null> => {
    try {
      setLoading(true);
      setError(null);
      const result = await request();
      return result;
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Ha ocurrido un error');
      return null;
    } finally {
      setLoading(false);
    }
  };

  return {
    loading,
    error,
    handleRequest,
  };
}