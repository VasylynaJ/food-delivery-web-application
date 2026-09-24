import { api } from './client';

export const cartApi = {
  get: () => api.get('/cart'),
  add: data => api.post('/cart/items', data),
  update: (id, data) => api.put('/cart/items/' + id, data),
  remove: id => api.delete('/cart/items/' + id),
};
