import axios from 'axios';

const api = axios.create({ baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api' });
api.interceptors.request.use(config => {
  const token = localStorage.getItem('food-token');
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});
export const authApi = {
  register: data => api.post('/auth/register', data),
  login: data => api.post('/auth/login', data),
};
export const restaurantsApi = {
  list: params => api.get('/restaurants', { params }),
  get: id => api.get(`/restaurants/${id}`),
  menu: id => api.get(`/restaurants/${id}/menu`),
};
export const cartApi = {
  get: () => api.get('/cart'),
  add: data => api.post('/cart/items', data),
  update: (id, data) => api.put(`/cart/items/${id}`, data),
  remove: id => api.delete(`/cart/items/${id}`),
};
export const ordersApi = {
  checkout: data => api.post('/orders', data),
  list: () => api.get('/orders'),
  get: id => api.get(`/orders/${id}`),
};
export const profileApi = { get: () => api.get('/profile'), update: data => api.put('/profile', data) };
export const adminApi = {
  restaurants: () => api.get('/admin/restaurants'),
  createRestaurant: data => api.post('/admin/restaurants', data),
  updateRestaurant: (id, data) => api.put(`/admin/restaurants/${id}`, data),
  deleteRestaurant: id => api.delete(`/admin/restaurants/${id}`),
  menu: id => api.get(`/admin/restaurants/${id}/menu`),
  createMenuItem: (id, data) => api.post(`/admin/restaurants/${id}/menu`, data),
  updateMenuItem: (restaurantId, id, data) => api.put(`/admin/restaurants/${restaurantId}/menu/${id}`, data),
  deleteMenuItem: (restaurantId, id) => api.delete(`/admin/restaurants/${restaurantId}/menu/${id}`),
  orders: () => api.get('/admin/orders'),
  updateOrder: (id, status) => api.patch(`/admin/orders/${id}/status`, { status }),
};
export function errorMessage(error) {
  return error.response?.data?.error || Object.values(error.response?.data?.fields || {})[0] || error.message || 'Something went wrong.';
}
