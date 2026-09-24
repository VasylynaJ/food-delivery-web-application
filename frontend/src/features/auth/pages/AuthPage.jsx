import { Link, useNavigate } from 'react-router-dom';
import { errorMessage } from '../../../utils/errorMessage';
import Alert from '../../../components/ui/Alert';
import PageLayout from '../../../components/layout/PageLayout';
import { useAuthenticate } from '../hooks/useAuthenticate';

export default function AuthPage({ mode }) {
  const navigate = useNavigate();
  const authenticate = useAuthenticate();

  function submit(event) {
    event.preventDefault();
    const values = Object.fromEntries(new FormData(event.currentTarget).entries());
    authenticate.mutate(
      { mode, values },
      { onSuccess: () => navigate('/restaurants') },
    );
  }

  return (
    <PageLayout>
      <div className="form-wrap">
        <p className="eyebrow">{mode === 'login' ? 'YOUR TABLE IS WAITING' : 'JOIN GATHER'}</p>
        <h1>{mode === 'login' ? 'Welcome back' : 'Create your account'}</h1>
        <p className="muted">{mode === 'login' ? 'Sign in to pick up where you left off.' : 'A few details, then let’s find something delicious.'}</p>
        <Alert>{authenticate.error && errorMessage(authenticate.error)}</Alert>
        <form className="form" onSubmit={submit}>
          {mode === 'register' && <label>Full name<input name="fullName" required maxLength="120" autoComplete="name" /></label>}
          <label>Email<input name="email" type="email" required autoComplete="email" /></label>
          <label>Password<input name="password" type="password" required minLength="8" maxLength="72" autoComplete={mode === 'login' ? 'current-password' : 'new-password'} /></label>
          <button className="button" disabled={authenticate.isPending}>
            {authenticate.isPending ? 'Please wait…' : mode === 'login' ? 'Sign in' : 'Create account'}
          </button>
        </form>
        <p className="switch-auth">
          {mode === 'login'
            ? <>New here? <Link to="/register">Create an account</Link></>
            : <>Already have an account? <Link to="/login">Sign in</Link></>}
        </p>
      </div>
    </PageLayout>
  );
}

