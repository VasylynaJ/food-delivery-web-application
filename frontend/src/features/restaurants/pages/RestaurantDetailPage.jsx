import { useState } from 'react';
import { useParams } from 'react-router-dom';
import { errorMessage } from '../../../utils/errorMessage';
import Alert from '../../../components/ui/Alert';
import PageLayout from '../../../components/layout/PageLayout';
import money from '../../../utils/money';
import MenuItemRow from '../components/MenuItemRow';
import { useAddCartItem } from '../../cart/hooks/useCart';
import { useAuth } from '../../../app/providers';
import { useRestaurant, useRestaurantMenu } from '../hooks/useRestaurantQueries';

export default function RestaurantDetailPage() {
  const { id } = useParams();
  const { user } = useAuth();
  const restaurantQuery = useRestaurant(id);
  const menuQuery = useRestaurantMenu(id);
  const addCartItem = useAddCartItem(user?.id);
  const [added, setAdded] = useState('');

  function add(item) {
    addCartItem.mutate(
      { menuItemId: item.id, quantity: 1 },
      { onSuccess: () => setAdded(item.name + ' added to your bag') },
    );
  }

  const error = restaurantQuery.error || menuQuery.error || addCartItem.error;

  return (
    <PageLayout>
      <Alert>{error && errorMessage(error)}</Alert>
      {(restaurantQuery.isPending || menuQuery.isPending) && <div className="empty">Loading restaurant…</div>}
      {restaurantQuery.data && (
        <>
          <div className="detail-hero">
            <div>
              <p className="eyebrow">{restaurantQuery.data.cuisine} · ★ {restaurantQuery.data.rating}</p>
              <h1>{restaurantQuery.data.name}</h1>
              <p>{restaurantQuery.data.description}</p>
              <small>{restaurantQuery.data.openingHours} · Delivery {money(restaurantQuery.data.deliveryFee)}</small>
            </div>
            <div className="detail-art" style={{ backgroundImage: restaurantQuery.data.imageUrl ? 'url(' + restaurantQuery.data.imageUrl + ')' : undefined }}>
              Made with care
            </div>
          </div>
          <Alert type="success">{added}</Alert>
          <h2 className="section-title">The menu</h2>
          <div className="menu-list">
            {(menuQuery.data ?? []).map(item => <MenuItemRow item={item} onAdd={add} key={item.id} />)}
          </div>
        </>
      )}
    </PageLayout>
  );
}

