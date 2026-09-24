import { useQuery } from '@tanstack/react-query';
import { restaurantApi } from '../../../api/restaurantApi';
import { restaurantKeys } from './queryKeys';

export function useRestaurants(filters) {
  return useQuery({
    queryKey: restaurantKeys.list(filters),
    queryFn: async () => (await restaurantApi.list(filters)).data,
  });
}

export function useRestaurant(id) {
  return useQuery({
    queryKey: restaurantKeys.detail(id),
    queryFn: async () => (await restaurantApi.get(id)).data,
    enabled: Boolean(id),
  });
}

export function useRestaurantMenu(id) {
  return useQuery({
    queryKey: restaurantKeys.menu(id),
    queryFn: async () => (await restaurantApi.menu(id)).data,
    enabled: Boolean(id),
  });
}

