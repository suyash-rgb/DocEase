import { useState } from 'react';
import Navbar from './components/Navbar';
import Hero from './components/Hero';
import Features from './components/Features';
import ChatbotPopup from './components/ChatbotPopup';
import Footer from './components/Footer';

export default function App() {
  const [chatVisible, setChatVisible] = useState(false);

  return (
    <>
      <Navbar />
      <Hero />
      <Features />

      {chatVisible && <ChatbotPopup onClose={() => setChatVisible(false)} />}

      {/* Logo toggle button below chatbot */}
      <div className="fixed bottom-2 right-6 z-40">
        <button onClick={() => setChatVisible(!chatVisible)} title="Toggle Chatbot">
          <img src="/logo.png" alt="DocEase Logo" className="h-15 w-12 hover:scale-105 transition-transform" />
        </button>
      </div>

      <Footer />
    </>
  );
}