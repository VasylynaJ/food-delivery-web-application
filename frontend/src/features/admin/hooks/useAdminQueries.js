import { useQuery } from '@tanstack/react-query';
import { adminApi } from '../../../api/adminApi';
import { adminKeys } from './queryKeys';

export function useAdminRestaurants(userId) {
  return useQuery({
    queryKey: adminKeys.restaurantList(userId),
    queryFn: async () => (await adminApi.restaurants()).data,
    enabled: Boolean(userId),
  });
}

export function useAdminMenu(userId, restaurantId) {
  return useQuery({
    queryKey: adminKeys.menu(userId, restaurantId),
    queryFn: async () => (await adminApi.menu(restaurantId)).data,
    enabled: Boolean(userId && restaurantId),
  });
}

export function useAdminOrders(userId) {
  return useQuery({
    queryKey: adminKeys.orders(userId),
    queryFn: async () => (await adminApi.orders()).data,
    enabled: Boolean(userId),
  });
}

