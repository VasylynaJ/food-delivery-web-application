import { useAuth } from '../../../app/providers';
import { errorMessage } from '../../../utils/errorMessage';
import Alert from '../../../components/ui/Alert';
import PageLayout from '../../../components/layout/PageLayout';
import money from '../../../utils/money';
import { useOrders } from '../hooks/useOrders';
import OrdersList from '../components/OrdersList';

export default function OrdersPage() {
  const { user } = useAuth();
  const ordersQuery = useOrders(user?.id);
  const data = ordersQuery.data ?? [];

  return (
    <PageLayout>
      <div className="page-heading"><div><p className="eyebrow">YOUR GATHER HISTORY</p><h1>My orders</h1></div></div>
      <Alert>{ordersQuery.error && errorMessage(ordersQuery.error)}</Alert>
      {ordersQuery.isPending && <div className="empty">Loading your orders…</div>}
      {ordersQuery.data !== undefined && (
        <OrdersList orders={data} money={money} />
      )}
      {ordersQuery.data !== undefined && !data.length && <div className="empty">No orders yet. Your next favorite is out there.</div>}
    </PageLayout>
  );
}

