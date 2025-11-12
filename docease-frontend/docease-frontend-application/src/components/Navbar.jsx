import { useState } from 'react';
import { Link } from 'react-router-dom';

export default function Navbar() {
  const [menuOpen, setMenuOpen] = useState(false);

  return (
    <nav className="bg-green-600 text-white px-6 py-4 shadow-md">
      <div className="flex justify-between items-center">
        {/* Logo and Brand */}
        <div className="flex items-center gap-3">
          <img src="/logo_cropped.png" alt="DocEase Logo" className="h-10 w-10" />
          <h1 className="text-xl font-bold">
            <Link to="/">DocEase</Link>
          </h1>
        </div>

        {/* Hamburger Icon */}
        <button
          className="md:hidden focus:outline-none"
          onClick={() => setMenuOpen(!menuOpen)}
        >
          <svg
            className="w-6 h-6"
            fill="none"
            stroke="currentColor"
            viewBox="0 0 24 24"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth={2}
              d={menuOpen ? 'M6 18L18 6M6 6l12 12' : 'M4 6h16M4 12h16M4 18h16'}
            />
          </svg>
        </button>

        {/* Desktop Menu */}
        <ul className="hidden md:flex gap-6 font-medium px-4">
          <li><a href="#features">Features</a></li>
          <li><Link to="/pricing">Pricing</Link></li>
          <li><a href="#about">About</a></li>
          <li><a href="#contact">Contact Us</a></li>
          <li><a href="#login">Log In</a></li>
          <li><Link to="/signup-selection">Sign Up</Link></li>
        </ul>
      </div>

      {/* Mobile Menu */}
      {menuOpen && (
        <ul className="md:hidden mt-4 space-y-2 font-medium">
          <li><a href="#features">Features</a></li>
          <li><Link to="/pricing">Pricing</Link></li>
          <li><a href="#about">About</a></li>
          <li><a href="#contact">Contact Us</a></li>
          <li><a href="#login">Log In</a></li>
          <li><Link to="/signup-selection">Sign Up</Link></li>
        </ul>
      )}
    </nav>
  );
}