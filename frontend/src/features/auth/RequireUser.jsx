import { Link } from 'react-router-dom';
import { useAuth } from '../../app/providers';
import PageLayout from '../../components/layout/PageLayout';

export default function RequireUser({ children, admin = false }) {
  const { user } = useAuth();

  if (!user) {
    return <PageLayout><div className="empty"><h2>Sign in to continue</h2><Link className="button" to="/login">Sign in</Link></div></PageLayout>;
  }

  if (admin && user.role !== 'ADMIN') {
    return <PageLayout><div className="empty">This area is for administrators.</div></PageLayout>;
  }

  return children;
}

