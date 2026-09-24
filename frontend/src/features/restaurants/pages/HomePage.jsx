import { Link } from 'react-router-dom';
import PageLayout from '../../../components/layout/PageLayout';

export default function HomePage() {
  return (
    <PageLayout>
      <section className="hero">
        <div className="hero-copy">
          <p className="eyebrow">GOOD FOOD, DELIVERED</p>
          <h1>Make tonight<br />taste <em>better.</em></h1>
          <p className="intro">Discover neighborhood favorites and have something lovely delivered to your door.</p>
          <Link className="button" to="/restaurants">Explore restaurants <span>↗</span></Link>
          <div className="note">Curated local kitchens <b>·</b> Made for sharing</div>
        </div>
        <div className="hero-art">
          <div className="sun" />
          <div className="art-label">A little joy<br />in every bite.</div>
          <div className="plate"><span>🥗</span></div>
          <div className="art-stamp">FRESH<br />&amp; LOCAL</div>
        </div>
      </section>
      <section className="home-bottom">
        <div>
          <p className="eyebrow">A GOOD PLACE TO START</p>
          <h2>Find your next favorite.</h2>
        </div>
        <Link to="/restaurants">Browse all restaurants ↗</Link>
      </section>
    </PageLayout>
  );
}

