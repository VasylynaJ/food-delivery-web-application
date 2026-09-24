export const adminKeys = {
  all: ['private', 'admin'],
  restaurants: userId => ['private', 'admin', userId, 'restaurants'],
  restaurantList: userId => ['private', 'admin', userId, 'restaurants', 'list'],
  menu: (userId, restaurantId) => ['private', 'admin', userId, 'restaurants', restaurantId, 'menu'],
  orders: userId => ['private', 'admin', userId, 'orders'],
};

