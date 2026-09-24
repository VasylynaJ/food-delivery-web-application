import { useMutation } from '@tanstack/react-query';
import { authApi } from '../../../api/authApi';
import { useAuth } from '../../../app/providers';

export function useAuthenticate() {
  const { save } = useAuth();

  return useMutation({
    mutationFn: async ({ mode, values }) => (
      mode === 'login'
        ? (await authApi.login(values)).data
        : (await authApi.register(values)).data
    ),
    onSuccess: save,
  });
}

