-- Create the database and switch to it
CREATE DATABASE docease;

USE docease;

-- 1. Roles table (to support role-based access for Admin, Doctor, Patient)
CREATE TABLE roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL  -- e.g., 'Admin', 'Doctor', 'Patient'
);

-- 2. Users table with fields for authentication, JWT handling, and MFA support.
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role_id INT NOT NULL,
    mfa_enabled BOOLEAN DEFAULT FALSE,
    mfa_secret VARCHAR(255) NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- 3. Doctors table (extending user details for doctors)
CREATE TABLE doctors (
    doctor_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    specialization VARCHAR(100) NOT NULL,
    availability TEXT, -- could store schedule as JSON or a formatted string
    consultation_fee DECIMAL(10,2),
    profile_description TEXT,
    phone VARCHAR(20),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- 4. Patients table (extending user details for patients)
CREATE TABLE patients (
    patient_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    medical_history TEXT,
    date_of_birth DATE,
    gender VARCHAR(10),
    phone VARCHAR(20),
    address VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- 5. Appointments table (linking patients with doctors)
CREATE TABLE appointments (
    appointment_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    appointment_date DATETIME NOT NULL,
    status VARCHAR(50) NOT NULL,  -- e.g., 'pending', 'confirmed', 'cancelled'
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id),
    FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id)
);

-- 6. Medical Records table (storing prescriptions, reports, and doctor’s notes)
CREATE TABLE medical_records (
    record_id INT AUTO_INCREMENT PRIMARY KEY,
    appointment_id INT NULL, -- optional link to appointment
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    prescription TEXT,
    report_url VARCHAR(255),  -- path or URL to the uploaded report
    doctor_notes TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id),
    FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id),
    FOREIGN KEY (appointment_id) REFERENCES appointments(appointment_id)
);

-- 7. Invoices table (handling billing and payment tracking)
CREATE TABLE invoices (
    invoice_id INT AUTO_INCREMENT PRIMARY KEY,
    appointment_id INT NOT NULL,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_status VARCHAR(50) NOT NULL,  -- e.g., 'paid', 'pending'
    invoice_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (appointment_id) REFERENCES appointments(appointment_id),
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id),
    FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id)
);

-- 8. Chat Messages table for secure messaging between users (e.g., for appointment follow-ups)
CREATE TABLE chat_messages (
    message_id INT AUTO_INCREMENT PRIMARY KEY,
    appointment_id INT,  -- optional context link to an appointment
    sender_id INT NOT NULL,  -- references a user (sender)
    receiver_id INT NOT NULL,  -- references a user (receiver)
    message TEXT NOT NULL,
    sent_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (sender_id) REFERENCES users(user_id),
    FOREIGN KEY (receiver_id) REFERENCES users(user_id)
);

-- 9. Symptom Queries Table for the AI-Powered Symptom Checker feature
CREATE TABLE symptom_queries (
    query_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    symptoms TEXT NOT NULL, -- user-entered symptoms (can be stored as a comma-separated list or JSON)
    recommended_specialist VARCHAR(100) NOT NULL,  -- suggested specialist based on the symptoms
    guidance TEXT, -- personalized healthcare guidance
    query_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id)
);

-- 10. Health Tips Table (storing general health tips, seasonal alerts, and doctor recommendations)
CREATE TABLE health_tips (
    tip_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    tip_date DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 11. Patient Notifications Table (linking tip notifications to patients)
CREATE TABLE patient_notifications (
    notification_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    tip_id INT,  -- can be null if the notification is custom
    message TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id),
    FOREIGN KEY (tip_id) REFERENCES health_tips(tip_id)
);

-- 12. Audit Logs Table (tracking system actions for enhanced security and compliance)
CREATE TABLE audit_logs (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    action VARCHAR(255) NOT NULL,
    details TEXT,
    log_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

ALTER TABLE doctors ADD COLUMN image_url VARCHAR(255);

SELECT * FROM roles;

INSERT INTO roles (name) VALUES ('ADMIN');
INSERT INTO roles (name) VALUES ('DOCTOR');
INSERT INTO roles (name) VALUES ('PATIENT');

ALTER TABLE doctors ADD COLUMN medical_license VARCHAR(100);