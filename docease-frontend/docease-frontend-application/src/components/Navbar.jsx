export default function Navbar() {
  return (
    <nav className="bg-green-600 text-white px-6 py-4 flex justify-between items-center shadow-md">
      <h1 className="text-xl font-bold">DocEase</h1>
      <ul className="flex gap-6 font-medium">
        <li><a href="#features">Features</a></li>
        <li><a href="#pricing">Pricing</a></li>
        <li><a href="#about">About</a></li>
        <li><a href="#contact">Contact Us</a></li>
        <li><a href="#login">Log In</a></li>
        <li><a href="#signup">Sign Up</a></li>
      </ul>
    </nav>
  );
}