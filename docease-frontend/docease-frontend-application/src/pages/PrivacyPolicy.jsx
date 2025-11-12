import React from 'react';

export default function PrivacyPolicy() {
  return (
    <div className="min-h-screen bg-gray-50 px-6 py-10 text-gray-800">
      <div className="max-w-4xl mx-auto bg-white shadow-md rounded-lg p-8">
        <h1 className="text-2xl font-bold mb-6 text-center text-green-700">Privacy Policy</h1>

        <section className="mb-6">
          <p>
            This Privacy Policy explains how <strong>DocEase</strong> collects, uses, and protects your personal
            information when you use our platform. By accessing or using DocEase, you agree to the terms outlined
            below.
          </p>
        </section>

        <section className="mb-6">
          <h2 className="text-lg font-semibold text-gray-700 mb-2">1. Information We Collect</h2>
          <ul className="list-disc ml-6 text-sm space-y-1">
            <li>Personal details such as name, email, phone number, and address</li>
            <li>Medical history, prescriptions, and diagnostic reports (for patients)</li>
            <li>Professional and availability details (for doctors)</li>
            <li>Administrative and hospital affiliation data (for admins)</li>
          </ul>
        </section>

        <section className="mb-6">
          <h2 className="text-lg font-semibold text-gray-700 mb-2">2. How We Use Your Information</h2>
          <ul className="list-disc ml-6 text-sm space-y-1">
            <li>To manage appointments and medical records</li>
            <li>To facilitate secure communication between users</li>
            <li>To generate invoices and track payments</li>
            <li>To provide personalized health tips and alerts</li>
            <li>To improve platform performance and user experience</li>
          </ul>
        </section>

        <section className="mb-6">
          <h2 className="text-lg font-semibold text-gray-700 mb-2">3. Data Sharing & Disclosure</h2>
          <p className="text-sm">
            We do not sell or rent your personal data. Information is shared only with authorized personnel or
            service providers under strict confidentiality, and only when necessary to deliver our services or comply
            with legal obligations.
          </p>
        </section>

        <section className="mb-6">
          <h2 className="text-lg font-semibold text-gray-700 mb-2">4. Data Security</h2>
          <p className="text-sm">
            We take your privacy seriously and implement appropriate technical and organizational measures to protect
            your data. Our systems are designed to prevent unauthorized access, ensure data integrity, and maintain
            confidentiality. While no system is entirely immune to risks, we continuously monitor and update our
            security practices to align with industry standards and evolving threats.
          </p>
        </section>

        <section className="mb-6">
          <h2 className="text-lg font-semibold text-gray-700 mb-2">5. Your Rights & Choices</h2>
          <p className="text-sm">
            You have the right to access, update, or delete your personal information. You may also opt out of
            non-essential communications. For any privacy-related requests, please contact us using the details
            below.
          </p>
        </section>

        <section className="mb-6">
          <h2 className="text-lg font-semibold text-gray-700 mb-2">6. Data Retention</h2>
          <p className="text-sm">
            We retain your data only as long as necessary to fulfill the purposes outlined in this policy or as
            required by law. You may request deletion of your data at any time, subject to legal and operational
            obligations.
          </p>
        </section>

        <section className="mb-6">
          <h2 className="text-lg font-semibold text-gray-700 mb-2">7. Cookies & Tracking</h2>
          <p className="text-sm">
            DocEase may use cookies and similar technologies to enhance your experience, maintain session state, and
            analyze usage patterns. You can manage your cookie preferences through your browser settings.
          </p>
        </section>

        <section className="mb-6">
          <h2 className="text-lg font-semibold text-gray-700 mb-2">8. Changes to This Policy</h2>
          <p className="text-sm">
            We may update this Privacy Policy from time to time. Any changes will be posted on this page with a
            revised effective date. Continued use of the platform constitutes acceptance of the updated policy.
          </p>
        </section>

        <section>
          <h2 className="text-lg font-semibold text-gray-700 mb-2">9. Contact Us</h2>
          <p className="text-sm">
            If you have any questions or concerns about this Privacy Policy, please contact us at:
          </p>
          <ul className="mt-2 text-sm ml-6 list-disc">
            <li>Email: privacy@docease.health</li>
            <li>Phone: [Insert Contact Number]</li>
            <li>Address: [Insert Office Address]</li>
          </ul>
        </section>
      </div>
    </div>
  );
}