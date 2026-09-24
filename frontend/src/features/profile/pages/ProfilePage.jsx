import { useState } from 'react';
import { useAuth } from '../../../app/providers';
import { errorMessage } from '../../../utils/errorMessage';
import Alert from '../../../components/ui/Alert';
import PageLayout from '../../../components/layout/PageLayout';
import { useProfile, useUpdateProfile } from '../hooks/useProfile';

export default function ProfilePage() {
  const { user } = useAuth();
  const profileQuery = useProfile(user?.id);
  const updateProfile = useUpdateProfile(user?.id);
  const [message, setMessage] = useState('');
  const data = profileQuery.data;

  function save(event) {
    event.preventDefault();
    updateProfile.mutate(
      { fullName: new FormData(event.currentTarget).get('fullName') },
      { onSuccess: () => setMessage('Profile saved.') },
    );
  }

  const error = profileQuery.error || updateProfile.error;

  return (
    <PageLayout>
      <div className="form-wrap">
        <p className="eyebrow">ACCOUNT</p>
        <h1>Your profile</h1>
        <Alert>{error && errorMessage(error)}</Alert>
        {profileQuery.isPending && <div className="empty">Loading your profile…</div>}
        <Alert type="success">{message}</Alert>
        {data && (
          <form className="form" onSubmit={save}>
            <label>Full name<input name="fullName" required maxLength="120" defaultValue={data.fullName} /></label>
            <label>Email<input value={data.email} disabled readOnly /></label>
            <label>Role<input value={data.role} disabled readOnly /></label>
            <button className="button" disabled={updateProfile.isPending}>
              {updateProfile.isPending ? 'Saving…' : 'Save profile'}
            </button>
          </form>
        )}
      </div>
    </PageLayout>
  );
}

