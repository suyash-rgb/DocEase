export default function Pricing() {
  const plans = [
    {
      emoji: "🩺",
      name: "Pro",
      description: "For individual doctors and small clinics.",
      tiers: [
        { duration: "1 Month", price: "₹499" },
        { duration: "3 Months", price: "₹1,299" },
        { duration: "6 Months", price: "₹2,499" },
        { duration: "1 Year", price: "₹4,799" },
      ],
    },
    {
      emoji: "🏥",
      name: "Enterprise",
      description: "Tailored for hospitals and multi-specialty setups.",
      tiers: [
        { duration: "1 Month", price: "₹1,999" },
        { duration: "3 Months", price: "₹5,499" },
        { duration: "6 Months", price: "₹10,499" },
        { duration: "1 Year", price: "₹19,999" },
      ],
    },
  ];

  return (
    <section id="pricing" className="py-16 px-6 bg-green-50">
      <h2 className="text-3xl font-bold text-green-700 text-center mb-10">💳 Pricing Plans</h2>
      <div className="grid md:grid-cols-2 gap-8 max-w-5xl mx-auto">
        {plans.map((plan) => (
          <div
            key={plan.name}
            className="bg-white border border-green-200 rounded-lg shadow-md p-6 hover:shadow-xl transition-shadow duration-300"
          >
            <div className="text-4xl mb-2">{plan.emoji}</div>
            <h3 className="text-xl font-bold text-green-600 mb-1">{plan.name}</h3>
            <p className="text-sm text-gray-600 mb-4">{plan.description}</p>

            <ul className="space-y-3">
              {plan.tiers.map((tier, idx) => (
                <li
                  key={idx}
                  className="flex justify-between items-center border border-green-100 rounded px-4 py-2 hover:bg-green-50"
                >
                  <span className="font-medium text-gray-800">{tier.duration}</span>
                  <span className="text-green-700 font-semibold">{tier.price}</span>
                </li>
              ))}
            </ul>

            <button className="mt-6 w-full bg-green-600 text-white py-2 px-4 rounded hover:bg-green-700 transition">
              Choose {plan.name}
            </button>
          </div>
        ))}
      </div>
    </section>
  );
}