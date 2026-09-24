import { api } from './client';

export const adminApi = {
  restaurants: () => api.get('/admin/restaurants'),
  createRestaurant: data => api.post('/admin/restaurants', data),
  updateRestaurant: (id, data) => api.put('/admin/restaurants/' + id, data),
  deleteRestaurant: id => api.delete('/admin/restaurants/' + id),
  menu: id => api.get('/admin/restaurants/' + id + '/menu'),
  createMenuItem: (id, data) => api.post('/admin/restaurants/' + id + '/menu', data),
  updateMenuItem: (restaurantId, id, data) => api.put('/admin/restaurants/' + restaurantId + '/menu/' + id, data),
  deleteMenuItem: (restaurantId, id) => api.delete('/admin/restaurants/' + restaurantId + '/menu/' + id),
  orders: () => api.get('/admin/orders'),
  updateOrder: (id, status) => api.patch('/admin/orders/' + id + '/status', { status }),
};
