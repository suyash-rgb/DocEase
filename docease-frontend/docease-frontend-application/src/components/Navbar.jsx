import { Link } from 'react-router-dom';

export default function Navbar() {
  return (
    <nav className="bg-green-600 text-white px-6 py-4 flex justify-between items-center shadow-md">
      <div className="flex items-center gap-3">
        <img src="/logo_cropped.png" alt="DocEase Logo" className="h-15 w-10" />
        <h1 className="text-xl font-bold"><Link to="/">DocEase</Link></h1>
      </div>

      <ul className="flex gap-6 font-medium px-4">
        <li><a href="#features">Features</a></li>
        <li><Link to="/pricing">Pricing</Link></li>
        <li><a href="#about">About</a></li>
        <li><a href="#contact">Contact Us</a></li>
        <li><a href="#login">Log In</a></li>
        <li><Link to="/signup-selection">Sign Up</Link></li>
      </ul>
    </nav>
  );
}