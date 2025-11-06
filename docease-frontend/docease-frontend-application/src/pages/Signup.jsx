import React from 'react';
import DoctorSignup from '../components/DoctorSignup';

export default function DoctorSignupPage() {
  return (
    <main className="min-h-screen bg-gray-50 flex items-center justify-center p-6">
      <section className="w-full max-w-4xl">
        {/* Optional page header */}
        <div className="mb-6 text-center">
          <h1 className="text-3xl font-extrabold text-green-700">Create your doctor account</h1>
          <p className="text-sm text-gray-600 mt-1">Join DocEase — manage appointments, connect with patients, and grow your practice.</p>
        </div>

        {/* Render the signup component (card handles its own layout) */}
        <DoctorSignup />
      </section> 
    </main>
  );
}