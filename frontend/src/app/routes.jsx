import { Route, Routes } from 'react-router-dom';
import AdminPage from '../features/admin/AdminPage';
import AuthPage from '../features/auth/pages/AuthPage';
import RequireUser from '../features/auth/RequireUser';
import CartPage from '../features/cart/pages/CartPage';
import CheckoutPage from '../features/cart/pages/CheckoutPage';
import HomePage from '../features/restaurants/pages/HomePage';
import RestaurantDetailPage from '../features/restaurants/pages/RestaurantDetailPage';
import RestaurantsPage from '../features/restaurants/pages/RestaurantsPage';
import OrderDetailPage from '../features/orders/pages/OrderDetailPage';
import OrdersPage from '../features/orders/pages/OrdersPage';
import ProfilePage from '../features/profile/pages/ProfilePage';

export default function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<HomePage />} />
      <Route path="/restaurants" element={<RestaurantsPage />} />
      <Route path="/restaurants/:id" element={<RestaurantDetailPage />} />
      <Route path="/login" element={<AuthPage mode="login" />} />
      <Route path="/register" element={<AuthPage mode="register" />} />
      <Route path="/cart" element={<RequireUser><CartPage /></RequireUser>} />
      <Route path="/checkout" element={<RequireUser><CheckoutPage /></RequireUser>} />
      <Route path="/orders" element={<RequireUser><OrdersPage /></RequireUser>} />
      <Route path="/orders/:id" element={<RequireUser><OrderDetailPage /></RequireUser>} />
      <Route path="/profile" element={<RequireUser><ProfilePage /></RequireUser>} />
      <Route path="/admin" element={<RequireUser admin><AdminPage /></RequireUser>} />
      <Route path="*" element={<HomePage />} />
    </Routes>
  );
}

