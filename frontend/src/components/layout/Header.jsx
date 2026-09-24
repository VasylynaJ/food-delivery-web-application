import { useAuth } from '../../app/providers';
import { Link, NavLink } from 'react-router-dom';

export default function Header() {
  const { user, logout } = useAuth();

  return (
    <header className="topbar">
      <Link className="brand" to="/">gather<span>.</span></Link>
      <nav>
        <NavLink to="/restaurants">Discover</NavLink>
        {user && <NavLink to="/orders">Orders</NavLink>}
        {user && <NavLink to="/profile">Profile</NavLink>}
        {user?.role === 'ADMIN' && <NavLink to="/admin">Admin</NavLink>}
        <NavLink className="bag-link" to="/cart">Bag</NavLink>
        {user
          ? <button className="link-button" onClick={logout}>Sign out</button>
          : <NavLink to="/login">Sign in</NavLink>}
      </nav>
    </header>
  );
}
