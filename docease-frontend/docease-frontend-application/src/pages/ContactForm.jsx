import { useState } from 'react';

export default function ContactForm() {
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    mobile: '',
    organization: '',
    profession: '',
    city: '',
    country: '',
    product: '',
    message: '',
    preferredTime: '',
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log('Form submitted:', formData);
    // Add API call or validation logic here
  };

  return (
    <section className="bg-blue-50 py-16 px-6">
      <h2 className="text-3xl font-bold text-center text-gray-800 mb-10">Contact Us</h2>
      <form
        onSubmit={handleSubmit}
        className="max-w-3xl mx-auto bg-white p-8 rounded-lg shadow-md grid gap-6"
      >
        <Input label="Name" name="name" required value={formData.name} onChange={handleChange} />
        <Input label="Email" name="email" type="email" required value={formData.email} onChange={handleChange} />
        <Input label="Mobile number" name="mobile" required value={formData.mobile} onChange={handleChange} />
        <Input label="Clinic/Hospital/Org Name" name="organization" required value={formData.organization} onChange={handleChange} />

        <Select
          label="Profession"
          name="profession"
          options={['Doctor', 'Admin', 'Receptionist', 'Other']}
          required
          value={formData.profession}
          onChange={handleChange}
        />

        <Input label="City" name="city" required value={formData.city} onChange={handleChange} />
        <Input label="Country/Region" name="country" required value={formData.country} onChange={handleChange} />

        <Select
          label="Select Product"
          name="product"
          options={['DocEase Basic', 'DocEase Pro', 'DocEase Enterprise']}
          value={formData.product}
          onChange={handleChange}
        />

        <Textarea label="Message" name="message" required value={formData.message} onChange={handleChange} />
        <Input label="Preferred Time for Call back" name="preferredTime" value={formData.preferredTime} onChange={handleChange} />

        <button type="submit" className="bg-orange-500 text-white py-2 px-4 rounded hover:bg-orange-600 transition">
          Submit
        </button>
      </form>
    </section>
  );
}

function Input({ label, name, type = 'text', required, value, onChange }) {
  return (
    <div>
      <label className="block text-sm font-medium text-gray-700 mb-1">
        {label}{required && <span className="text-red-500">*</span>}
      </label>
      <input
        type={type}
        name={name}
        required={required}
        value={value}
        onChange={onChange}
        className="w-full border border-gray-300 rounded px-3 py-2 text-sm"
      />
    </div>
  );
}

function Select({ label, name, options, required, value, onChange }) {
  return (
    <div>
      <label className="block text-sm font-medium text-gray-700 mb-1">
        {label}{required && <span className="text-red-500">*</span>}
      </label>
      <select
        name={name}
        required={required}
        value={value}
        onChange={onChange}
        className="w-full border border-gray-300 rounded px-3 py-2 text-sm bg-white"
      >
        <option value="">Please Select</option>
        {options.map((opt, idx) => (
          <option key={idx} value={opt}>{opt}</option>
        ))}
      </select>
    </div>
  );
}

function Textarea({ label, name, required, value, onChange }) {
  return (
    <div>
      <label className="block text-sm font-medium text-gray-700 mb-1">
        {label}{required && <span className="text-red-500">*</span>}
      </label>
      <textarea
        name={name}
        required={required}
        value={value}
        onChange={onChange}
        rows={4}
        className="w-full border border-gray-300 rounded px-3 py-2 text-sm"
      />
    </div>
  );
}