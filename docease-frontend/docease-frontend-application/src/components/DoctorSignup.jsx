import { useEffect, useMemo, useState } from 'react';
// If you want the entrance animation, install framer-motion and uncomment lines below
// import { motion } from 'framer-motion';

const initialForm = {
  username: '',
  email: '',
  password: '',
  specialization: '',
  consultationFee: '',
  profileDescription: '',
  phone: '',
  imageUrl: '',
  medicalLicense: '',
};

export default function DoctorSignup() {
  const [form, setForm] = useState(initialForm);
  const [touched, setTouched] = useState({});
  const [submitting, setSubmitting] = useState(false);
  const [status, setStatus] = useState(null); // { type: 'success'|'error', message: string }
  const [imagePreview, setImagePreview] = useState('');

  useEffect(() => {
    if (form.imageUrl) setImagePreview(form.imageUrl);
    else setImagePreview('');
  }, [form.imageUrl]);

  const validators = useMemo(() => ({
    username: (v) => v.trim() !== '' || 'Username is required',
    email: (v) => /\S+@\S+\.\S+/.test(v) || 'Enter a valid email',
    password: (v) => v.length >= 6 || 'Password must be at least 6 characters',
    specialization: (v) => v.trim() !== '' || 'Specialization is required',
    consultationFee: (v) => v === '' || (!isNaN(v) && Number(v) >= 0) || 'Fee must be 0 or positive number',
    phone: (v) => /^\d{10}$/.test(v) || 'Phone must be exactly 10 digits',
    // profileDescription, imageUrl, medicalLicense are optional
  }), []);

  const errors = useMemo(() => {
    const e = {};
    for (const key of Object.keys(validators)) {
      const res = validators[key](form[key]);
      if (res !== true) e[key] = res;
    }
    return e;
  }, [form, validators]);

  const isValid = Object.keys(errors).length === 0;

  function handleChange(e) {
    const { name, value } = e.target;
    setForm((s) => ({ ...s, [name]: value }));
  }

  function handleBlur(e) {
    const { name } = e.target;
    setTouched((t) => ({ ...t, [name]: true }));
  }

  // Mock API call - replace with real API integration
  async function registerDoctor(payload) {
    await new Promise((r) => setTimeout(r, 1100));
    // Simulate random failure for demo
    if (payload.username.toLowerCase().includes('fail')) {
      const err = new Error('Simulated server error');
      err.body = { message: 'Server rejected registration' };
      throw err;
    }
    return { ok: true, id: Math.floor(Math.random() * 10000) };
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setTouched({
      username: true,
      email: true,
      password: true,
      specialization: true,
      consultationFee: true,
      phone: true,
    });

    if (!isValid) {
      setStatus({ type: 'error', message: 'Please fix validation errors before submitting.' });
      return;
    }

    setSubmitting(true);
    setStatus(null);
    try {
      const payload = {
        username: form.username.trim(),
        email: form.email.trim(),
        password: form.password,
        specialization: form.specialization.trim(),
        consultationFee: form.consultationFee === '' ? null : Number(form.consultationFee),
        profileDescription: form.profileDescription.trim(),
        phone: form.phone,
        imageUrl: form.imageUrl.trim() || null,
        medicalLicense: form.medicalLicense.trim() || null,
      };
      await registerDoctor(payload);
      setStatus({ type: 'success', message: 'Registration successful. Welcome to DocEase!' });
      setForm(initialForm);
      setTouched({});
      setImagePreview('');
      // Optionally redirect to login or dashboard here
    } catch (err) {
      setStatus({ type: 'error', message: err?.body?.message || err.message || 'Registration failed. Please try again.' });
    } finally {
      setSubmitting(false);
    }
  }

  return (
    // Replace <div> with <motion.div> and set initial/animate props if using framer-motion
    <div className="min-h-screen flex items-center justify-center bg-gray-50 p-4">
      <div className="w-full max-w-2xl">
        <div className="bg-white rounded-2xl shadow-lg overflow-hidden">
          <div className="flex gap-6 p-6 md:p-8">
            {/* Left: Branding / Image Preview */}
            <aside className="w-36 hidden md:flex flex-col items-center justify-start gap-4">
              <div
                className="w-20 h-20 rounded-full bg-gradient-to-br from-green-100 to-green-50 flex items-center justify-center shadow-sm"
                aria-hidden
              >
                {/* simple medical icon */}
                <svg width="34" height="34" viewBox="0 0 24 24" fill="none" aria-hidden>
                  <path d="M12 2v20M2 12h20" stroke="#16A34A" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
                </svg>
              </div>
              <p className="text-xs text-gray-600 text-center">Join DocEase — make care accessible</p>

              <div className="w-full">
                {imagePreview ? (
                  <img src={imagePreview} alt="Profile preview" className="w-full h-24 object-cover rounded-md border" />
                ) : (
                  <div className="w-full h-24 rounded-md border border-dashed border-gray-200 flex items-center justify-center text-xs text-gray-400">
                    Profile preview
                  </div>
                )}
              </div>
            </aside>

            {/* Right: Form */}
            <div className="flex-1">
              <header className="mb-4">
                <h2 className="text-2xl font-semibold text-green-700">Join DocEase as a Doctor</h2>
                <p className="text-sm text-gray-600">Create your account to manage appointments and connect with patients.</p>
              </header>

              <form onSubmit={handleSubmit} noValidate>
                {/* Personal info */}
                <fieldset className="mb-4 border-b pb-4">
                  <legend className="sr-only">Personal information</legend>

                  <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
                    <div>
                      <label htmlFor="username" className="block text-xs font-medium text-gray-700">Username *</label>
                      <input
                        id="username"
                        name="username"
                        value={form.username}
                        onChange={handleChange}
                        onBlur={handleBlur}
                        className={`mt-1 block w-full rounded-md border px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-green-200 ${
                          touched.username && errors.username ? 'border-red-500' : 'border-gray-200'
                        }`}
                        aria-invalid={!!(touched.username && errors.username)}
                        aria-describedby={touched.username && errors.username ? 'username-error' : undefined}
                      />
                      {touched.username && errors.username && (
                        <p id="username-error" className="mt-1 text-xs text-red-600">{errors.username}</p>
                      )}
                    </div>

                    <div>
                      <label htmlFor="email" className="block text-xs font-medium text-gray-700">Email *</label>
                      <input
                        id="email"
                        name="email"
                        type="email"
                        value={form.email}
                        onChange={handleChange}
                        onBlur={handleBlur}
                        className={`mt-1 block w-full rounded-md border px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-green-200 ${
                          touched.email && errors.email ? 'border-red-500' : 'border-gray-200'
                        }`}
                        aria-invalid={!!(touched.email && errors.email)}
                        aria-describedby={touched.email && errors.email ? 'email-error' : undefined}
                      />
                      {touched.email && errors.email && (
                        <p id="email-error" className="mt-1 text-xs text-red-600">{errors.email}</p>
                      )}
                    </div>

                    <div>
                      <label htmlFor="password" className="block text-xs font-medium text-gray-700">Password *</label>
                      <input
                        id="password"
                        name="password"
                        type="password"
                        value={form.password}
                        onChange={handleChange}
                        onBlur={handleBlur}
                        className={`mt-1 block w-full rounded-md border px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-green-200 ${
                          touched.password && errors.password ? 'border-red-500' : 'border-gray-200'
                        }`}
                        aria-invalid={!!(touched.password && errors.password)}
                        aria-describedby={touched.password && errors.password ? 'password-error' : undefined}
                      />
                      {touched.password && errors.password && (
                        <p id="password-error" className="mt-1 text-xs text-red-600">{errors.password}</p>
                      )}
                    </div>

                    <div>
                      <label htmlFor="phone" className="block text-xs font-medium text-gray-700">Phone *</label>
                      <input
                        id="phone"
                        name="phone"
                        inputMode="numeric"
                        value={form.phone}
                        onChange={handleChange}
                        onBlur={handleBlur}
                        placeholder="10 digits"
                        className={`mt-1 block w-full rounded-md border px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-green-200 ${
                          touched.phone && errors.phone ? 'border-red-500' : 'border-gray-200'
                        }`}
                        aria-invalid={!!(touched.phone && errors.phone)}
                        aria-describedby={touched.phone && errors.phone ? 'phone-error' : undefined}
                      />
                      {touched.phone && errors.phone && (
                        <p id="phone-error" className="mt-1 text-xs text-red-600">{errors.phone}</p>
                      )}
                    </div>
                  </div>
                </fieldset>

                {/* Professional info */}
                <fieldset className="mb-4 border-b pb-4">
                  <legend className="sr-only">Professional information</legend>

                  <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
                    <div>
                      <label htmlFor="specialization" className="block text-xs font-medium text-gray-700">Specialization *</label>
                      <input
                        id="specialization"
                        name="specialization"
                        value={form.specialization}
                        onChange={handleChange}
                        onBlur={handleBlur}
                        className={`mt-1 block w-full rounded-md border px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-green-200 ${
                          touched.specialization && errors.specialization ? 'border-red-500' : 'border-gray-200'
                        }`}
                        aria-invalid={!!(touched.specialization && errors.specialization)}
                        aria-describedby={touched.specialization && errors.specialization ? 'specialization-error' : undefined}
                      />
                      {touched.specialization && errors.specialization && (
                        <p id="specialization-error" className="mt-1 text-xs text-red-600">{errors.specialization}</p>
                      )}
                    </div>

                    <div>
                      <label htmlFor="consultationFee" className="block text-xs font-medium text-gray-700">
                        Consultation Fee
                        <span className="ml-2 text-xs text-gray-400"> (INR)</span>
                      </label>
                      <input
                        id="consultationFee"
                        name="consultationFee"
                        type="number"
                        min="0"
                        step="0.01"
                        value={form.consultationFee}
                        onChange={handleChange}
                        onBlur={handleBlur}
                        className={`mt-1 block w-full rounded-md border px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-green-200 ${
                          touched.consultationFee && errors.consultationFee ? 'border-red-500' : 'border-gray-200'
                        }`}
                        aria-invalid={!!(touched.consultationFee && errors.consultationFee)}
                        aria-describedby={touched.consultationFee && errors.consultationFee ? 'fee-error' : undefined}
                      />
                      {touched.consultationFee && errors.consultationFee && (
                        <p id="fee-error" className="mt-1 text-xs text-red-600">{errors.consultationFee}</p>
                      )}
                    </div>

                    <div className="md:col-span-2">
                      <label htmlFor="profileDescription" className="block text-xs font-medium text-gray-700">Profile Description</label>
                      <textarea
                        id="profileDescription"
                        name="profileDescription"
                        value={form.profileDescription}
                        onChange={handleChange}
                        rows="3"
                        className="mt-1 block w-full rounded-md border px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-green-200 border-gray-200"
                      />
                    </div>

                    <div>
                      <label htmlFor="imageUrl" className="block text-xs font-medium text-gray-700">Image URL</label>
                      <input
                        id="imageUrl"
                        name="imageUrl"
                        type="url"
                        value={form.imageUrl}
                        onChange={handleChange}
                        className="mt-1 block w-full rounded-md border px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-green-200 border-gray-200"
                        placeholder="https://example.com/photo.jpg"
                        aria-describedby="image-help"
                      />
                      <p id="image-help" className="mt-1 text-xs text-gray-400">Paste a public image URL to preview.</p>
                    </div>

                    <div>
                      <label htmlFor="medicalLicense" className="block text-xs font-medium text-gray-700">Medical License</label>
                      <input
                        id="medicalLicense"
                        name="medicalLicense"
                        value={form.medicalLicense}
                        onChange={handleChange}
                        className="mt-1 block w-full rounded-md border px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-green-200 border-gray-200"
                        placeholder="License number (optional)"
                      />
                    </div>
                  </div>
                </fieldset>

                {/* status */}
                {status && (
                  <div
                    role="status"
                    className={`mb-4 px-4 py-3 rounded-md text-sm ${status.type === 'success' ? 'bg-green-50 text-green-700 border border-green-100' : 'bg-red-50 text-red-700 border border-red-100'}`}
                  >
                    {status.message}
                  </div>
                )}

                {/* actions */}
                <div className="flex flex-col md:flex-row gap-3 items-center justify-between">
                  <button
                    type="submit"
                    disabled={submitting || !isValid}
                    className={`w-full md:w-auto inline-flex items-center justify-center gap-2 px-5 py-2 rounded-md text-white font-medium transition ${
                      submitting || !isValid
                        ? 'bg-green-300 cursor-not-allowed'
                        : 'bg-[#16A34A] hover:bg-[#22C55E] focus:ring-2 focus:ring-green-200'
                    }`}
                    aria-disabled={submitting || !isValid}
                  >
                    {submitting ? (
                      <>
                        <svg className="animate-spin -ml-1 mr-2 h-4 w-4 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                          <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                          <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8z"></path>
                        </svg>
                        Registering…
                      </>
                    ) : (
                      'Create account'
                    )}
                  </button>

                  <div className="text-sm text-gray-600">
                    <span>Already have an account? </span>
                    <a href="/login" className="text-green-700 hover:underline">Log in</a>
                  </div>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}