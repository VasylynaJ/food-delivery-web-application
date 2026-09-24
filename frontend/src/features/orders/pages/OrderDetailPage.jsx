import { useParams } from 'react-router-dom';
import { useAuth } from '../../../app/providers';
import { errorMessage } from '../../../utils/errorMessage';
import Alert from '../../../components/ui/Alert';
import PageLayout from '../../../components/layout/PageLayout';
import money from '../../../utils/money';
import { useOrder } from '../hooks/useOrders';

export default function OrderDetailPage() {
  const { id } = useParams();
  const { user } = useAuth();
  const orderQuery = useOrder(user?.id, id);
  const order = orderQuery.data;

  return (
    <PageLayout>
      <Alert>{orderQuery.error && errorMessage(orderQuery.error)}</Alert>
      {orderQuery.isPending && <div className="empty">Loading order details…</div>}
      {order && (
        <>
          <p className="eyebrow">ORDER #{order.id}</p>
          <h1 className="order-title">Order details</h1>
          <div className="detail-grid">
            <section className="panel">
              <div className="status">{order.status.replaceAll('_', ' ')}</div>
              <h3>Delivering to</h3>
              <p>{order.street}<br />{order.city} {order.postalCode}</p>
              <h3>Items</h3>
              {order.items.map((item, index) => (
                <p className="order-item" key={index}>
                  <span>{item.quantity} × {item.itemName}</span>
                  <b>{money(item.unitPrice * item.quantity)}</b>
                </p>
              ))}
            </section>
            <aside className="summary">
              <p><span>Subtotal</span><b>{money(order.subtotal)}</b></p>
              <p><span>Delivery</span><b>{money(order.deliveryFee)}</b></p>
              <p className="sum-total"><span>Total</span><b>{money(order.total)}</b></p>
            </aside>
          </div>
        </>
      )}
    </PageLayout>
  );
}

