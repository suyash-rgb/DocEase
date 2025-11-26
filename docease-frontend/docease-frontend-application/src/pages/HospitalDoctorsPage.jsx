// import React, { useState } from 'react';
// import { getDoctorsInHospital } from './services/hospitalService';

// export default function HospitalDoctorsPage() {
//   const [hospitalId, setHospitalId] = useState('');
//   const [doctors, setDoctors] = useState([]);

//   const handleFetch = async () => {
//     try {
//       const result = await getDoctorsInHospital(hospitalId);
//       setDoctors(result);
//     } catch (err) {
//       console.error(err);
//       alert('Error fetching doctors');
//     }
//   };

//   return (
//     <div className="min-h-screen flex flex-col items-center justify-center bg-gray-50 p-6">
//       <h2 className="text-xl font-bold mb-4">Doctors in Hospital</h2>
//       <div className="flex gap-2 mb-4">
//         <input
//           type="text"
//           placeholder="Hospital ID"
//           value={hospitalId}
//           onChange={(e) => setHospitalId(e.target.value)}
//           className="border rounded px-3 py-2"
//         />
//         <button
//           onClick={handleFetch}
//           className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700"
//         >
//           Fetch
//         </button>
//       </div>
//       <ul className="w-full max-w-md space-y-2">
//         {doctors.map((doc) => (
//           <li key={doc.id} className="border rounded px-3 py-2 bg-white shadow-sm">
//             {doc.name} — {doc.specialization}
//           </li>
//         ))}
//       </ul>
//     </div>
//   );
// }