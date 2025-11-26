import React from 'react';
import { useNavigate } from 'react-router-dom';

export default function UserRoleSelectionPage() {
  const navigate = useNavigate();

  const roles = [
    {
      label: 'Hospital Admin',
      description: 'Manage hospital operations, staff, and patient records.',
      route: '/signup/admin',
    },
    {
      label: 'Doctor',
      description: 'Access patient data, prescribe treatments, and manage appointments.',
      route: '/signup/doctor',
    },
    {
      label: 'Patient',
      description: 'Book appointments, view prescriptions, and access your health records.',
      route: '/signup/patient',
    },
  ];

  return (
    <div className="min-h-screen bg-gray-50 flex flex-col items-center justify-center px-4 py-8">
      <h1 className="text-6xl font-bold text-gray-800 mb-6">Select Profile</h1>
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 w-full max-w-4xl">
        {roles.map((role) => (
          <div
            key={role.label}
            onClick={() => navigate(role.route)}
            className="cursor-pointer bg-white shadow-md rounded-lg p-6 hover:shadow-lg transition duration-200 border border-gray-200"
          >
            <h2 className="text-xl font-semibold text-blue-700 mb-2">{role.label}</h2>
            <p className="text-sm text-gray-600">{role.description}</p>
          </div>
        ))}
      </div>
    </div>
  );
}