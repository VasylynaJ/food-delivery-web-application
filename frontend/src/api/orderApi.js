import { api } from './client';

export const orderApi = {
  checkout: data => api.post('/orders', data),
  list: () => api.get('/orders'),
  get: id => api.get('/orders/' + id),
};
