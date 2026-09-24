export function errorMessage(error) {
  return error.response?.data?.error
    || Object.values(error.response?.data?.fields || {})[0]
    || error.message
    || 'Something went wrong.';
}
