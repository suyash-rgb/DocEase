import { Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar';
import Hero from './components/Hero';
import Features from './components/Features';
import Footer from './components/Footer';
import ChatbotPopup from './components/ChatbotPopup';
import Pricing from './pages/Pricing';
import DoctorSignupPage from './pages/Signup';
import { useState } from 'react';

export default function App() {
  const [chatVisible, setChatVisible] = useState(false);

  return (
    <>
      <Navbar />

      {/* Chatbot popup is available globally */}
      {chatVisible && <ChatbotPopup onClose={() => setChatVisible(false)} />}
      {!chatVisible && (
        <div className="fixed bottom-2 right-6 z-40">
          <button onClick={() => setChatVisible(true)} title="Toggle Chatbot">
            <img src="/logo.png" alt="DocEase Logo" className="h-15 w-12 hover:scale-105 transition-transform" />
          </button>
        </div>
      )}

      <Routes>
        <Route
          path="/"
          element={
            <>
              <Hero />
              <Features />
            </>
          }
        />
        <Route path="/pricing" element={<Pricing />} />
        <Route path="/signup/doctor" element={<DoctorSignupPage />} />
        {/* Optional: redirect or 404 route can go here */}
      </Routes>

      <Footer />
    </>
  );
}