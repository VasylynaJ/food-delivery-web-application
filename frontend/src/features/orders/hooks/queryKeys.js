export const orderKeys = {
  all: ['private', 'orders'],
  user: userId => ['private', 'orders', userId],
  list: userId => ['private', 'orders', userId, 'list'],
  detail: (userId, orderId) => ['private', 'orders', userId, 'detail', String(orderId)],
};
