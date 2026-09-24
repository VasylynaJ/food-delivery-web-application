import { Link } from 'react-router-dom';
import money from '../../../utils/money';

export default function CartSummary({ cart }) {
  return (
    <aside className="summary">
      <h3>Order summary</h3>
      <p><span>Subtotal</span><b>{money(cart.subtotal)}</b></p>
      <p><span>Delivery</span><b>{money(cart.deliveryFee)}</b></p>
      <p className="sum-total"><span>Total</span><b>{money(cart.total)}</b></p>
      <Link className={'button ' + (!cart.items.length ? 'disabled' : '')} to={cart.items.length ? '/checkout' : '/cart'}>Continue to checkout</Link>
      <small>Delivery fee is calculated by the server.</small>
    </aside>
  );
}

