export default function Footer() {
  return (
    <footer className="bg-green-600 text-white px-6 py-8 mt-12">
      <div className="max-w-6xl mx-auto grid md:grid-cols-3 gap-6 text-sm">
        
        {/* Brand & Tagline */}
        <div>
          <h2 className="text-lg font-bold mb-2">DocEase</h2>
          <p>Your health, streamlined. Empowering patients and doctors with smart scheduling and secure records.</p>
        </div>

        {/* Quick Links */}
        <div>
          <h3 className="font-semibold mb-2">Quick Links</h3>
          <ul className="space-y-1">
            <li><a href="#features" className="hover:underline">Features</a></li>
            <li><a href="#about" className="hover:underline">About</a></li>
            <li><a href="#contact" className="hover:underline">Contact</a></li>
            <li><a href="/privacy" className="hover:underline">Privacy Policy</a></li>
          </ul>
        </div>

        {/* Contact Info */}
        <div>
          <h3 className="font-semibold mb-2">Support</h3>
          <p>Email: <a href="mailto:support@docease.in" className="underline">support@docease.in</a></p>
          <p>Phone: +91-98765-43210</p>
        </div>
      </div>

      {/* Bottom Bar */}
      <div className="text-center text-xs mt-8 border-t border-green-500 pt-4">
        &copy; {new Date().getFullYear()} DocEase. All rights reserved.
      </div>
    </footer>
  );
}