import { Link } from 'react-router-dom';
import { errorMessage } from '../../../utils/errorMessage';
import Alert from '../../../components/ui/Alert';
import PageLayout from '../../../components/layout/PageLayout';
import CartLine from '../components/CartLine';
import CartSummary from '../components/CartSummary';
import { useAuth } from '../../../app/providers';
import { useCart, useRemoveCartItem, useUpdateCartItem } from '../hooks/useCart';

export default function CartPage() {
  const { user } = useAuth();
  const cartQuery = useCart(user?.id);
  const updateItem = useUpdateCartItem(user?.id);
  const removeItem = useRemoveCartItem(user?.id);
  const cart = cartQuery.data;
  const error = cartQuery.error || updateItem.error || removeItem.error;

  function remove(id) {
    removeItem.mutate(id);
  }

  function change(item, delta) {
    const quantity = item.quantity + delta;
    if (quantity < 1) return remove(item.menuItemId);
    updateItem.mutate({ menuItemId: item.menuItemId, quantity });
  }

  return (
    <PageLayout>
      <div className="page-heading">
        <div><p className="eyebrow">READY WHEN YOU ARE</p><h1>Your bag</h1></div>
        <Link to="/restaurants">Add more ↗</Link>
      </div>
      <Alert>{error && errorMessage(error)}</Alert>
      {cartQuery.isPending && <div className="empty">Loading your bag…</div>}
      {cart && (
        <div className="checkout-layout">
          <div className="cart-lines">
            {cart.items.map(item => (
              <CartLine item={item} onChange={change} onRemove={remove} key={item.menuItemId} />
            ))}
            {!cart.items.length && <div className="empty">Your bag is empty. <Link to="/restaurants">Explore restaurants</Link></div>}
          </div>
          <CartSummary cart={cart} />
        </div>
      )}
    </PageLayout>
  );
}

