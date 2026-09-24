import { useMutation, useQueryClient } from '@tanstack/react-query';
import { adminApi } from '../../../api/adminApi';
import { cartKeys } from '../../cart/hooks/queryKeys';
import { orderKeys } from '../../orders/hooks/queryKeys';
import { restaurantKeys } from '../../restaurants/hooks/queryKeys';
import { adminKeys } from './queryKeys';

export function useSaveAdminRestaurant(userId) {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async ({ id, data }) => (
      id
        ? (await adminApi.updateRestaurant(id, data)).data
        : (await adminApi.createRestaurant(data)).data
    ),
    onSuccess: restaurant => {
      queryClient.setQueryData(restaurantKeys.detail(restaurant.id), restaurant);
      queryClient.setQueryData(adminKeys.restaurantList(userId), current => {
        if (!current) return current;
        const exists = current.some(item => item.id === restaurant.id);
        return exists
          ? current.map(item => item.id === restaurant.id ? restaurant : item)
          : [...current, restaurant];
      });
      queryClient.invalidateQueries({ queryKey: adminKeys.restaurants(userId) });
      queryClient.invalidateQueries({ queryKey: restaurantKeys.all });
    },
  });
}

export function useDeleteAdminRestaurant(userId) {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: id => adminApi.deleteRestaurant(id),
    onSuccess: (_response, restaurantId) => {
      queryClient.invalidateQueries({ queryKey: adminKeys.restaurants(userId) });
      queryClient.invalidateQueries({ queryKey: restaurantKeys.all });
      queryClient.invalidateQueries({ queryKey: cartKeys.all });
      queryClient.removeQueries({ queryKey: adminKeys.menu(userId, restaurantId) });
    },
  });
}

export function useSaveAdminMenuItem(userId) {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async ({ restaurantId, itemId, data }) => (
      itemId
        ? (await adminApi.updateMenuItem(restaurantId, itemId, data)).data
        : (await adminApi.createMenuItem(restaurantId, data)).data
    ),
    onSuccess: (_item, { restaurantId }) => {
      queryClient.invalidateQueries({ queryKey: adminKeys.menu(userId, restaurantId) });
      queryClient.invalidateQueries({ queryKey: restaurantKeys.menu(restaurantId) });
    },
  });
}

export function useDeleteAdminMenuItem(userId) {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: ({ restaurantId, itemId }) => adminApi.deleteMenuItem(restaurantId, itemId),
    onSuccess: (_response, { restaurantId }) => {
      queryClient.invalidateQueries({ queryKey: adminKeys.menu(userId, restaurantId) });
      queryClient.invalidateQueries({ queryKey: restaurantKeys.menu(restaurantId) });
      queryClient.invalidateQueries({ queryKey: cartKeys.all });
    },
  });
}

export function useUpdateAdminOrderStatus(userId) {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: async ({ orderId, status }) =>
      (await adminApi.updateOrder(orderId, status)).data,
    onSuccess: updatedOrder => {
      queryClient.setQueryData(adminKeys.orders(userId), current => (
        current?.map(order => order.id === updatedOrder.id ? updatedOrder : order)
      ));
      queryClient.invalidateQueries({ queryKey: adminKeys.orders(userId) });
      queryClient.invalidateQueries({ queryKey: orderKeys.all });
    },
  });
}

