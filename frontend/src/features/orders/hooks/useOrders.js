import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { orderApi } from '../../../api/orderApi';
import { cartKeys } from '../../cart/hooks/queryKeys';
import { orderKeys } from './queryKeys';

export function useOrders(userId) {
  return useQuery({
    queryKey: orderKeys.list(userId),
    queryFn: async () => (await orderApi.list()).data,
    enabled: Boolean(userId),
  });
}

export function useOrder(userId, orderId) {
  return useQuery({
    queryKey: orderKeys.detail(userId, orderId),
    queryFn: async () => (await orderApi.get(orderId)).data,
    enabled: Boolean(userId && orderId),
  });
}

export function useCheckout(userId) {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async data => (await orderApi.checkout(data)).data,
    onSuccess: order => {
      if (!userId) return;
      queryClient.setQueryData(orderKeys.detail(userId, order.id), order);
      queryClient.invalidateQueries({ queryKey: orderKeys.list(userId) });
      queryClient.removeQueries({ queryKey: cartKeys.user(userId), exact: true });
    },
  });
}

