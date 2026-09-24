export default function AdminListRow({ title, subtitle, onEdit, onDelete }) {
  return (
    <div className="admin-row">
      <span><b>{title}</b><small>{subtitle}</small></span>
      <button onClick={onEdit}>Edit</button>
      <button onClick={onDelete}>Delete</button>
    </div>
  );
}
