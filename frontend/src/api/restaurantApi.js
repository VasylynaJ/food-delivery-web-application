import { api } from './client';

export const restaurantApi = {
  list: params => api.get('/restaurants', { params }),
  get: id => api.get('/restaurants/' + id),
  menu: id => api.get('/restaurants/' + id + '/menu'),
};
