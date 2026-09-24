export default function Alert({ children, type = 'error' }) {
  return children ? <div className={'alert ' + type}>{children}</div> : null;
}
