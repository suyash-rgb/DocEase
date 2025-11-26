import React, { useState } from 'react';
import { addDoctorToHospital } from '../services/hospitalService';

export default function AddDoctorPage() {
  const [hospitalId, setHospitalId] = useState('');
  const [doctorId, setDoctorId] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const updatedHospital = await addDoctorToHospital(hospitalId, doctorId);
      alert(`Doctor added. Hospital now has ${updatedHospital.doctors.length} doctors.`);
    } catch (err) {
      console.error(err);
      alert('Error adding doctor');
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50">
      <form
        onSubmit={handleSubmit}
        className="bg-white shadow-md rounded-lg p-6 w-full max-w-md space-y-4"
      >
        <h2 className="text-xl font-bold text-center">Add Doctor to Hospital</h2>
        <input
          type="text"
          placeholder="Hospital ID"
          value={hospitalId}
          onChange={(e) => setHospitalId(e.target.value)}
          className="w-full border rounded px-3 py-2"
          required
        />
        <input
          type="text"
          placeholder="Doctor ID"
          value={doctorId}
          onChange={(e) => setDoctorId(e.target.value)}
          className="w-full border rounded px-3 py-2"
          required
        />
        <button
          type="submit"
          className="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700"
        >
          Add Doctor
        </button>
      </form>
    </div>
  );
}