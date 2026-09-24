import { Link } from 'react-router-dom';

export default function OrdersList({ orders, money }) {
  return (
    <div className="order-list">
      {orders.map(order => (
        <Link className="order-card" to={'/orders/' + order.id} key={order.id}>
          <div>
            <span className="status">{order.status.replaceAll('_', ' ')}</span>
            <h3>Order #{order.id}</h3>
            <small>{new Date(order.createdAt).toLocaleString()}</small>
          </div>
          <b>{money(order.total)} ↗</b>
        </Link>
      ))}
    </div>
  );
}
