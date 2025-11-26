// import axios from 'axios';

// const API_BASE = '/api/hospitals';

// export const createHospital = async (hospitalData) => {
//   const response = await axios.post(API_BASE, hospitalData);
//   return response.data;
// };

// export const addDoctorToHospital = async (hospitalId, doctorId) => {
//   const response = await axios.post(`${API_BASE}/${hospitalId}/doctors`, { doctorId });
//   return response.data;
// };

// export const getDoctorsInHospital = async (hospitalId) => {
//   const response = await axios.get(`${API_BASE}/${hospitalId}/doctors`);
//   return response.data;
// };