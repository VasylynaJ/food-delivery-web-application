import { useEffect, useState } from 'react';
import { useAuth } from '../../app/providers';
import { errorMessage } from '../../utils/errorMessage';
import Alert from '../../components/ui/Alert';
import PageLayout from '../../components/layout/PageLayout';
import money from '../../utils/money';
import AdminListRow from './components/AdminListRow';
import {
  useAdminMenu,
  useAdminOrders,
  useAdminRestaurants,
} from './hooks/useAdminQueries';
import {
  useDeleteAdminMenuItem,
  useDeleteAdminRestaurant,
  useSaveAdminMenuItem,
  useSaveAdminRestaurant,
  useUpdateAdminOrderStatus,
} from './hooks/useAdminMutations';

export default function AdminPage() {
  const { user } = useAuth();
  const [selected, setSelected] = useState('');
  const [editing, setEditing] = useState(null);
  const restaurantsQuery = useAdminRestaurants(user?.id);
  const ordersQuery = useAdminOrders(user?.id);
  const menuQuery = useAdminMenu(user?.id, selected);
  const restaurants = restaurantsQuery.data ?? [];
  const orders = ordersQuery.data ?? [];
  const menu = menuQuery.data ?? [];
  const saveRestaurant = useSaveAdminRestaurant(user?.id);
  const deleteRestaurant = useDeleteAdminRestaurant(user?.id);
  const saveMenuItem = useSaveAdminMenuItem(user?.id);
  const deleteMenuItem = useDeleteAdminMenuItem(user?.id);
  const updateOrderStatus = useUpdateAdminOrderStatus(user?.id);

  useEffect(() => {
    if (!selected && restaurants.length) setSelected(String(restaurants[0].id));
  }, [selected, restaurants]);

  function restaurantSubmit(event) {
    event.preventDefault();
    const form = event.currentTarget;
    const data = Object.fromEntries(new FormData(form));
    data.rating = Number(data.rating);
    data.deliveryFee = Number(data.deliveryFee);
    data.open = data.open === 'true';

    saveRestaurant.mutate(
      { id: editing?.kind === 'restaurant' ? editing.id : undefined, data },
      {
        onSuccess: () => {
          setEditing(null);
          form.reset();
        },
      },
    );
  }

  function menuSubmit(event) {
    event.preventDefault();
    const form = event.currentTarget;
    const data = Object.fromEntries(new FormData(form));
    data.price = Number(data.price);
    data.available = true;

    saveMenuItem.mutate(
      {
        restaurantId: selected,
        itemId: editing?.kind === 'menu' ? editing.id : undefined,
        data,
      },
      {
        onSuccess: () => {
          setEditing(null);
          form.reset();
        },
      },
    );
  }

  function removeRestaurant(id) {
    if (confirm('Delete this restaurant and its menu?')) deleteRestaurant.mutate(id);
  }

  function setStatus(orderId, status) {
    updateOrderStatus.mutate({ orderId, status });
  }

  const error = [
    restaurantsQuery.error,
    ordersQuery.error,
    menuQuery.error,
    saveRestaurant.error,
    deleteRestaurant.error,
    saveMenuItem.error,
    deleteMenuItem.error,
    updateOrderStatus.error,
  ].find(Boolean);

  return (
    <PageLayout>
      <p className="eyebrow">OPERATIONS</p>
      <h1 className="admin-title">Admin dashboard</h1>
      <Alert>{error && errorMessage(error)}</Alert>

      <div className="admin-grid">
        <section className="panel">
          <h2>Restaurants</h2>
          {restaurantsQuery.isPending && <p className="muted">Loading restaurants…</p>}
          <form
            key={'restaurant-' + (editing?.kind === 'restaurant' ? editing.id : 'new')}
            className="admin-form"
            onSubmit={restaurantSubmit}
          >
            <h3>{editing?.kind === 'restaurant' ? 'Edit restaurant' : 'Add restaurant'}</h3>
            <input name="name" placeholder="Restaurant name" required defaultValue={editing?.kind === 'restaurant' ? editing.data.name : ''} />
            <input name="cuisine" placeholder="Cuisine" required defaultValue={editing?.kind === 'restaurant' ? editing.data.cuisine : ''} />
            <input name="description" placeholder="Description" defaultValue={editing?.kind === 'restaurant' ? editing.data.description : ''} />
            <div className="form-grid">
              <input name="rating" type="number" min="0" max="5" step="0.1" placeholder="Rating" aria-label="Rating" required defaultValue={editing?.kind === 'restaurant' ? editing.data.rating : '0'} />
              <input name="deliveryFee" type="number" min="0" step="0.01" placeholder="Delivery fee (€)" aria-label="Delivery fee (€)" required defaultValue={editing?.kind === 'restaurant' ? editing.data.deliveryFee : '0.00'} />
            </div>
            <input name="openingHours" placeholder="Opening hours" required defaultValue={editing?.kind === 'restaurant' ? editing.data.openingHours : ''} />
            <input name="imageUrl" placeholder="Image URL" defaultValue={editing?.kind === 'restaurant' ? editing.data.imageUrl : ''} />
            <select name="open" defaultValue={editing?.kind === 'restaurant' ? String(editing.data.open) : 'true'}>
              <option value="true">Open</option>
              <option value="false">Closed</option>
            </select>
            <button className="button small" disabled={saveRestaurant.isPending}>Save restaurant</button>
          </form>

          <div className="admin-list">
            {restaurants.map(restaurant => (
              <AdminListRow
                key={restaurant.id}
                title={restaurant.name}
                subtitle={restaurant.cuisine}
                onEdit={() => setEditing({ kind: 'restaurant', id: restaurant.id, data: restaurant })}
                onDelete={() => removeRestaurant(restaurant.id)}
              />
            ))}
          </div>
        </section>

        <section className="panel">
          <h2>Menu management</h2>
          <select value={selected} onChange={event => setSelected(event.target.value)}>
            {restaurants.map(restaurant => <option value={restaurant.id} key={restaurant.id}>{restaurant.name}</option>)}
          </select>
          {selected && menuQuery.isPending && <p className="muted">Loading menu…</p>}
          <form
            key={'menu-' + (editing?.kind === 'menu' ? editing.id : 'new')}
            className="admin-form"
            onSubmit={menuSubmit}
          >
            <h3>{editing?.kind === 'menu' ? 'Edit menu item' : 'Add menu item'}</h3>
            <input name="category" placeholder="Category" required defaultValue={editing?.kind === 'menu' ? editing.data.category : ''} />
            <input name="name" placeholder="Item name" required defaultValue={editing?.kind === 'menu' ? editing.data.name : ''} />
            <input name="description" placeholder="Description" defaultValue={editing?.kind === 'menu' ? editing.data.description : ''} />
            <div className="form-grid">
              <input name="price" type="number" min="0.01" step="0.01" placeholder="Price" required defaultValue={editing?.kind === 'menu' ? editing.data.price : ''} />
              <input name="imageUrl" placeholder="Image URL" defaultValue={editing?.kind === 'menu' ? editing.data.imageUrl : ''} />
            </div>
            <button className="button small" disabled={saveMenuItem.isPending}>Save menu item</button>
          </form>

          {menu.map(item => (
            <AdminListRow
              key={item.id}
              title={item.name}
              subtitle={item.category + ' · ' + money(item.price)}
              onEdit={() => setEditing({ kind: 'menu', id: item.id, data: item })}
              onDelete={() => deleteMenuItem.mutate({ restaurantId: selected, itemId: item.id })}
            />
          ))}
        </section>
      </div>

      <section className="panel admin-orders">
        <h2>Orders</h2>
        {ordersQuery.isPending && <p className="muted">Loading orders…</p>}
        {orders.map(order => (
          <div className="admin-order" key={order.id}>
            <span><b>#{order.id} · {money(order.total)}</b><small>{order.street}, {order.city}</small></span>
            <select
              value={order.status}
              onChange={event => setStatus(order.id, event.target.value)}
              disabled={updateOrderStatus.isPending}
            >
              {['PENDING', 'CONFIRMED', 'PREPARING', 'OUT_FOR_DELIVERY', 'DELIVERED', 'CANCELLED']
                .map(status => <option key={status}>{status}</option>)}
            </select>
          </div>
        ))}
        {ordersQuery.isSuccess && !orders.length && <p className="muted">New orders will appear here.</p>}
      </section>
    </PageLayout>
  );
}

