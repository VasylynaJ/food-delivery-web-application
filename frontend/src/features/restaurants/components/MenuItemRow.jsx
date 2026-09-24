import money from '../../../utils/money';

export default function MenuItemRow({ item, onAdd }) {
  return (
    <article className="menu-row">
      {item.imageUrl && <img className="menu-thumb" src={item.imageUrl} alt="" loading="lazy" />}
      <div className="menu-description">
        <span className="category">{item.category}</span>
        <h3>{item.name}</h3>
        <p>{item.description}</p>
        <strong>{money(item.price)}</strong>
      </div>
      <button className="add-button" onClick={() => onAdd(item)}>Add +</button>
    </article>
  );
}

