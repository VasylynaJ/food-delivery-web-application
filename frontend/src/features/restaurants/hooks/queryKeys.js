export const restaurantKeys = {
  all: ['restaurants'],
  lists: () => ['restaurants', 'list'],
  list: filters => ['restaurants', 'list', { q: filters.q ?? '', cuisine: filters.cuisine ?? '' }],
  detail: id => ['restaurants', 'detail', id],
  menu: id => ['restaurants', 'detail', id, 'menu'],
};

