import { Link } from 'react-router-dom';
import money from '../../../utils/money';

export default function RestaurantCard({ restaurant }) {
  return (
    <Link to={'/restaurants/' + restaurant.id} className="restaurant-card">
      <div className="restaurant-image" style={{ backgroundImage: restaurant.imageUrl ? 'url(' + restaurant.imageUrl + ')' : undefined }}>
        <span>{restaurant.open ? 'Open now' : 'Closed'}</span>
      </div>
      <div className="card-info">
        <div><h3>{restaurant.name}</h3><p>{restaurant.cuisine}</p></div>
        <b>★ {Number(restaurant.rating).toFixed(1)}</b>
      </div>
      <small>{restaurant.openingHours} · Delivery {money(restaurant.deliveryFee)}</small>
    </Link>
  );
}

