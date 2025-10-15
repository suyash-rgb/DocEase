export default function Features() {
  const features = [
    {
      emoji: "🗓️",
      title: "Smart Scheduling",
      description: "Real-time slot booking, rescheduling, and waitlist notifications.",
    },
    {
      emoji: "🔐",
      title: "Secure Medical Records",
      description: "Upload prescriptions, reports, and treatment notes securely.",
    },
    {
      emoji: "🤖",
      title: "AI Symptom Checker",
      description: "Get smart recommendations based on symptoms.",
    },
    {
      emoji: "🩺",
      title: "Find Specialists",
      description: "Find specialists by location, availability, and expertise.",
    },
    {
      emoji: "📺",
      title: "Waiting Room Queue",
      description: "Display patient queue on a TV and announce next appointments.",
    },
    {
      emoji: "👨‍⚕️👩‍⚕️",
      title: "Multi-Doctor Support",
      description: "Manage multiple doctors and departments with ease.",
    },
    {
      emoji: "⏰",
      title: "Availability Management",
      description: "Easily create available and busy timings to control appointment slots.",
    },
    {
      emoji: "🔄",
      title: "Shifting Appointments",
      description: "Reschedule multiple appointments and notify patients via SMS and Email.",
    },
    {
      emoji: "❌",
      title: "Cancellations",
      description: "Cancel appointments in bulk and inform patients instantly via SMS/Email.",
    }
  ];

  return (
    <section id="features" className="py-16 px-6 bg-white">
      <h2 className="text-3xl font-bold text-green-700 text-center mb-10">🧩 Features</h2>
      <div className="grid md:grid-cols-2 gap-8 max-w-5xl mx-auto">
        {features.map((feature, idx) => (
          <div
            key={idx}
            className="flex items-start gap-4 border border-green-100 rounded-lg p-4 hover:shadow-md transition"
          >
            <div className="text-3xl">{feature.emoji}</div>
            <div>
              <h3 className="text-xl font-semibold text-green-600 mb-1">{feature.title}</h3>
              <p className="text-gray-700 text-sm">{feature.description}</p>
            </div>
          </div>
        ))}
      </div>
    </section>
  );
}