import { Link } from 'react-router-dom';
import Header from './Header';

export default function PageLayout({ children }) {
  return (
    <>
      <Header />
      <main className="page">{children}</main>
      <footer>
        <Link className="brand" to="/">gather<span>.</span></Link>
        <span>A personal portfolio project by Vasylyna Shelepko</span>
      </footer>
    </>
  );
}
