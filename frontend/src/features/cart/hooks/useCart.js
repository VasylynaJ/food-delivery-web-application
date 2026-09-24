import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { cartApi } from '../../../api/cartApi';
import { cartKeys } from './queryKeys';

export function useCart(userId) {
  return useQuery({
    queryKey: cartKeys.user(userId),
    queryFn: async () => (await cartApi.get()).data,
    enabled: Boolean(userId),
  });
}

export function useAddCartItem(userId) {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async data => (await cartApi.add(data)).data,
    onSuccess: cart => {
      if (userId) queryClient.setQueryData(cartKeys.user(userId), cart);
    },
  });
}

export function useUpdateCartItem(userId) {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async ({ menuItemId, quantity }) =>
      (await cartApi.update(menuItemId, { quantity })).data,
    onSuccess: cart => {
      if (userId) queryClient.setQueryData(cartKeys.user(userId), cart);
    },
  });
}

export function useRemoveCartItem(userId) {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async menuItemId => (await cartApi.remove(menuItemId)).data,
    onSuccess: cart => {
      if (userId) queryClient.setQueryData(cartKeys.user(userId), cart);
    },
  });
}

