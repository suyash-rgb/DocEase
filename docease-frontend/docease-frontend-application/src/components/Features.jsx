export default function Features() {
  return (
    <section id="features" className="py-16 px-6 bg-white">
      <h3 className="text-3xl font-bold text-green-700 text-center mb-8">✨ Key Features</h3>
      <div className="grid md:grid-cols-2 gap-8 max-w-4xl mx-auto">
        <Feature
          emoji="🗓️"
          title="Smart Scheduling"
          description="Real-time slot booking, rescheduling, and waitlist notifications."
        />
        <Feature
          emoji="🔐"
          title="Secure Medical Records"
          description="Upload prescriptions, reports, and treatment notes securely."
        />
        <Feature
          emoji="🤖"
          title="AI Symptom Checker"
          description="Get smart recommendations based on symptoms."
        />
        <Feature
          emoji="🩺"
          title="Doctor Search & Filters"
          description="Find specialists by location, availability, and expertise."
        />
      </div>
    </section>
  );
}

function Feature({ emoji, title, description }) {
  return (
    <div className="flex items-start gap-3">
      <div className="text-2xl">{emoji}</div>
      <div>
        <h4 className="text-xl font-semibold text-green-600">{title}</h4>
        <p className="text-gray-700">{description}</p>
      </div>
    </div>
  );
}