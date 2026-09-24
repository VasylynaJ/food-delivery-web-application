import { useQueryClient } from '@tanstack/react-query';
import { createContext, useContext, useState } from 'react';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    try {
      return JSON.parse(localStorage.getItem('food-user'));
    } catch {
      return null;
    }
  });
  const queryClient = useQueryClient();

  const save = payload => {
    queryClient.removeQueries({ queryKey: ['private'] });
    localStorage.setItem('food-token', payload.token);
    const next = {
      id: payload.id,
      fullName: payload.fullName,
      email: payload.email,
      role: payload.role,
    };
    localStorage.setItem('food-user', JSON.stringify(next));
    setUser(next);
  };

  const logout = () => {
    queryClient.removeQueries({ queryKey: ['private'] });
    localStorage.removeItem('food-token');
    localStorage.removeItem('food-user');
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, save, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}

