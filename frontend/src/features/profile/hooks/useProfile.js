import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { profileApi } from '../../../api/profileApi';
import { profileKeys } from './queryKeys';

export function useProfile(userId) {
  return useQuery({
    queryKey: profileKeys.user(userId),
    queryFn: async () => (await profileApi.get()).data,
    enabled: Boolean(userId),
  });
}

export function useUpdateProfile(userId) {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async data => (await profileApi.update(data)).data,
    onSuccess: profile => {
      if (userId) queryClient.setQueryData(profileKeys.user(userId), profile);
    },
  });
}

