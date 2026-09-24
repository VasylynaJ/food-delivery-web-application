import { useNavigate } from 'react-router-dom';
import { errorMessage } from '../../../utils/errorMessage';
import Alert from '../../../components/ui/Alert';
import PageLayout from '../../../components/layout/PageLayout';
import { useAuth } from '../../../app/providers';
import { useCheckout } from '../../orders/hooks/useOrders';

export default function CheckoutPage() {
  const navigate = useNavigate();
  const { user } = useAuth();
  const checkout = useCheckout(user?.id);

  function submit(event) {
    event.preventDefault();
    const data = Object.fromEntries(new FormData(event.currentTarget));
    checkout.mutate(data, {
      onSuccess: order => navigate('/orders/' + order.id),
    });
  }

  return (
    <PageLayout>
      <div className="form-wrap">
        <p className="eyebrow">LAST STEP</p>
        <h1>Delivery details</h1>
        <p className="muted">Your order total is recalculated securely when placed.</p>
        <Alert>{checkout.error && errorMessage(checkout.error)}</Alert>
        <form className="form" onSubmit={submit}>
          <label>Address label<input name="label" placeholder="Home" maxLength="80" /></label>
          <label>Street address<input name="street" required maxLength="180" autoComplete="street-address" /></label>
          <div className="form-grid">
            <label>City<input name="city" required maxLength="100" autoComplete="address-level2" /></label>
            <label>Postal code<input name="postalCode" required maxLength="20" autoComplete="postal-code" /></label>
          </div>
          <label>Delivery instructions<textarea name="instructions" maxLength="500" rows="3" /></label>
          <button className="button" disabled={checkout.isPending}>{checkout.isPending ? 'Placing order…' : 'Place order'}</button>
        </form>
      </div>
    </PageLayout>
  );
}

