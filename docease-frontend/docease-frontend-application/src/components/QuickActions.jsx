import React from 'react';
import { motion } from 'framer-motion';

const container = {
  hidden: { opacity: 0, y: 6 },
  visible: { opacity: 1, y: 0, transition: { staggerChildren: 0.06, delayChildren: 0.06 } },
  exit: { opacity: 0, y: 4, transition: { duration: 0.18 } },
};

const item = {
  hidden: { opacity: 0, y: 6 },
  visible: { opacity: 1, y: 0, transition: { duration: 0.28 } },
};

export default function QuickActions({ onAction }) {
  return (
    <motion.div variants={container} initial="hidden" animate="visible" exit="exit" className="mt-3 flex flex-wrap gap-2">
      <motion.button variants={item} onClick={() => onAction('firstAid')} className="text-xs px-2 py-1 rounded bg-gray-100 hover:bg-gray-200 transition-colors">
        First Aid Guidance
      </motion.button> 

      <motion.button variants={item} onClick={() => onAction('symptomChecker')} className="text-xs px-2 py-1 rounded bg-green-50 text-green-700 border border-green-100 hover:bg-green-100 transition-colors">
        Symptoms Checker
      </motion.button>

      <motion.button variants={item} onClick={() => onAction('specialist')} className="text-xs px-2 py-1 rounded bg-gray-100 hover:bg-gray-200 transition-colors">
        Specialist Recommender
      </motion.button>

      <motion.button variants={item} onClick={() => onAction('medication')} className="text-xs px-2 py-1 rounded bg-gray-100 hover:bg-gray-200 transition-colors">
        Medication Info
      </motion.button>

      <motion.button variants={item} onClick={() => onAction('lab')} className="text-xs px-2 py-1 rounded bg-gray-100 hover:bg-gray-200 transition-colors">
        Lab Test Explainer
      </motion.button>

      <motion.button variants={item} onClick={() => onAction('talkDoctor')} className="text-xs px-2 py-1 rounded bg-gray-100 hover:bg-gray-200 transition-colors">
        Talk to a Doctor
      </motion.button>
    </motion.div>
  );
}