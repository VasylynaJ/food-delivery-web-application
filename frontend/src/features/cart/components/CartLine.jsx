import money from '../../../utils/money';

export default function CartLine({ item, onChange, onRemove }) {
  return (
    <div className="cart-line" key={item.menuItemId}>
      <div><h3>{item.name}</h3><p>{money(item.unitPrice)} each</p></div>
      <div className="quantity">
        <button onClick={() => onChange(item, -1)}>−</button>
        <span>{item.quantity}</span>
        <button onClick={() => onChange(item, 1)}>+</button>
      </div>
      <b>{money(item.lineTotal)}</b>
      <button className="remove" onClick={() => onRemove(item.menuItemId)} aria-label="Remove item">×</button>
    </div>
  );
}

