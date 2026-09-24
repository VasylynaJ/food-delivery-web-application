import { useState } from 'react';
import { errorMessage } from '../../../utils/errorMessage';
import Alert from '../../../components/ui/Alert';
import PageLayout from '../../../components/layout/PageLayout';
import RestaurantCard from '../components/RestaurantCard';
import { useRestaurants } from '../hooks/useRestaurantQueries';

export default function RestaurantsPage() {
  const [q, setQ] = useState('');
  const [cuisine, setCuisine] = useState('');
  const restaurantsQuery = useRestaurants({ q, cuisine });
  const data = restaurantsQuery.data ?? [];
  const hasData = restaurantsQuery.data !== undefined;
  const cuisines = [...new Set(data.map(restaurant => restaurant.cuisine))];

  return (
    <PageLayout>
      <div className="page-heading">
        <div><p className="eyebrow">LOCAL FAVORITES</p><h1>Restaurants</h1></div>
        <span>{data.length} places to explore</span>
      </div>
      <div className="filters">
        <input aria-label="Search restaurants" placeholder="Search restaurants" value={q} onChange={event => setQ(event.target.value)} />
        <select aria-label="Filter cuisine" value={cuisine} onChange={event => setCuisine(event.target.value)}>
          <option value="">All cuisines</option>
          {cuisines.map(value => <option key={value}>{value}</option>)}
        </select>
      </div>
      <Alert>{restaurantsQuery.error && errorMessage(restaurantsQuery.error)}</Alert>
      {restaurantsQuery.isPending && <div className="empty">Loading restaurants…</div>}
      {hasData && (
        <div className="restaurant-grid">
          {data.map(restaurant => <RestaurantCard restaurant={restaurant} key={restaurant.id} />)}
        </div>
      )}
      {hasData && !data.length && <div className="empty">No restaurants found. Try another search.</div>}
    </PageLayout>
  );
}

