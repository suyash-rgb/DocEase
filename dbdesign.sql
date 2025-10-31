-- ===================================================================
-- DOCEASE MVP DATABASE - FULL SCRIPT
-- MySQL 8.0+ Compatible
-- Run in order: creates DB → tables → constraints → indexes → seed
-- ===================================================================

-- Drop and recreate database (safe for dev)
-- 1. Drop database if exists
DROP DATABASE IF EXISTS docease_microservice;

CREATE DATABASE docease_microservice CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE docease_microservice;

-- ===================================================================
-- 1. ROLES
-- ===================================================================
CREATE TABLE roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

-- ===================================================================
-- 2. USERS (Authentication + MFA)
-- ===================================================================
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role_id INT NOT NULL,
    mfa_enabled BOOLEAN DEFAULT FALSE,
    mfa_secret VARCHAR(255) NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- ===================================================================
-- 3. DOCTORS
-- ===================================================================
CREATE TABLE doctors (
    doctor_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    specialization VARCHAR(100) NOT NULL,
    consultation_fee DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    profile_description TEXT,
    phone VARCHAR(20),
    image_url VARCHAR(255),
    medical_license VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- ===================================================================
-- 4. PATIENTS
-- ===================================================================
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

-- ===================================================================
-- 5. HOSPITALS (Multi-clinic support)
-- ===================================================================
CREATE TABLE hospitals (
    hospital_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(100),
    address VARCHAR(500),
    timezone VARCHAR(50) DEFAULT 'UTC',
    metadata JSON NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- ===================================================================
-- 6. DOCTOR <-> HOSPITAL (Many-to-Many)
-- ===================================================================
CREATE TABLE doctor_hospital (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    doctor_id INT NOT NULL,
    hospital_id INT NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id),
    FOREIGN KEY (hospital_id) REFERENCES hospitals(hospital_id),
    UNIQUE KEY uq_doctor_hospital (doctor_id, hospital_id)
);

-- ===================================================================
-- 7. APPOINTMENT SLOTS (Scheduling Engine)
-- ===================================================================
CREATE TABLE appointment_slots (
    slot_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    doctor_id INT NOT NULL,
    hospital_id INT NULL,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    duration_minutes INT NOT NULL,
    slot_status VARCHAR(50) NOT NULL DEFAULT 'available',
    source VARCHAR(50) NULL,
    hold_until DATETIME NULL,
    extra JSON NULL,
    FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id),
    FOREIGN KEY (hospital_id) REFERENCES hospitals(hospital_id),
    INDEX idx_doctor_start (doctor_id, start_time),
    INDEX idx_hospital_start (hospital_id, start_time)
);

-- ===================================================================
-- 8. APPOINTMENTS
-- ===================================================================
CREATE TABLE appointments (
    appointment_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    slot_id BIGINT NULL,
    appointment_date DATETIME NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'pending',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id),
    FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id),
    FOREIGN KEY (slot_id) REFERENCES appointment_slots(slot_id)
);

-- ===================================================================
-- 9. MEDICAL RECORDS
-- ===================================================================
CREATE TABLE medical_records (
    record_id INT AUTO_INCREMENT PRIMARY KEY,
    appointment_id INT NULL,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    prescription TEXT,
    report_url VARCHAR(255),
    doctor_notes TEXT,
    encrypted BOOLEAN DEFAULT TRUE,
    kms_key_id VARCHAR(255) NULL,
    version INT DEFAULT 1,
    deleted BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (appointment_id) REFERENCES appointments(appointment_id),
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id),
    FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id)
);

-- ===================================================================
-- 10. INVOICES
-- ===================================================================
CREATE TABLE invoices (
    invoice_id INT AUTO_INCREMENT PRIMARY KEY,
    appointment_id INT NOT NULL,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    currency CHAR(3) DEFAULT 'USD',
    payment_status VARCHAR(50) NOT NULL DEFAULT 'pending',
    payment_provider VARCHAR(100) NULL,
    payment_reference VARCHAR(255) NULL,
    refunded BOOLEAN DEFAULT FALSE,
    due_date DATETIME NULL,
    invoice_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (appointment_id) REFERENCES appointments(appointment_id),
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id),
    FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id)
);

-- ===================================================================
-- 11. CHAT MESSAGES
-- ===================================================================
CREATE TABLE chat_messages (
    message_id INT AUTO_INCREMENT PRIMARY KEY,
    appointment_id INT NULL,
    sender_id INT NOT NULL,
    receiver_id INT NOT NULL,
    message TEXT NOT NULL,
    attachment_file_id BIGINT NULL,
    sent_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    moderated BOOLEAN DEFAULT FALSE,
    deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (appointment_id) REFERENCES appointments(appointment_id),
    FOREIGN KEY (sender_id) REFERENCES users(user_id),
    FOREIGN KEY (receiver_id) REFERENCES users(user_id)
);

-- ===================================================================
-- 12. FILE STORAGE (Reports, Attachments)
-- ===================================================================
CREATE TABLE file_storage (
    file_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    owner_user_id INT NULL,
    appointment_id INT NULL,
    patient_id INT NULL,
    doctor_id INT NULL,
    hospital_id INT NULL,
    file_name VARCHAR(255),
    file_path VARCHAR(1000) NOT NULL,
    file_size BIGINT,
    mime_type VARCHAR(100),
    encrypted BOOLEAN DEFAULT TRUE,
    kms_key_id VARCHAR(255) NULL,
    uploaded_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_user_id) REFERENCES users(user_id),
    FOREIGN KEY (appointment_id) REFERENCES appointments(appointment_id),
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id),
    FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id),
    FOREIGN KEY (hospital_id) REFERENCES hospitals(hospital_id)
);

-- Add FK for chat attachment
ALTER TABLE chat_messages
    ADD FOREIGN KEY (attachment_file_id) REFERENCES file_storage(file_id);

-- ===================================================================
-- 13. SYMPTOM QUERIES (AI Checker)
-- ===================================================================
CREATE TABLE symptom_queries (
    query_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    symptoms JSON NOT NULL,
    recommended_specialist VARCHAR(100),
    guidance TEXT,
    input_json JSON NULL,
    model_version VARCHAR(50) NULL,
    processed BOOLEAN DEFAULT FALSE,
    processed_at DATETIME NULL,
    query_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id)
);

-- ===================================================================
-- 14. HEALTH TIPS & NOTIFICATIONS
-- ===================================================================
CREATE TABLE health_tips (
    tip_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    tip_date DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE patient_notifications (
    notification_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    tip_id INT NULL,
    message TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id),
    FOREIGN KEY (tip_id) REFERENCES health_tips(tip_id)
);

-- ===================================================================
-- 15. WAITLIST
-- ===================================================================
CREATE TABLE waitlists (
    waitlist_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    doctor_id INT NOT NULL,
    patient_id INT NOT NULL,
    requested_for_datetime DATETIME NULL,
    requested_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    notified BOOLEAN DEFAULT FALSE,
    notify_channel VARCHAR(50) DEFAULT 'email',
    FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id),
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id),
    INDEX idx_waitlist_doctor (doctor_id, notified)
);

-- ===================================================================
-- 16. SLOT HOLDS (Temporary Booking)
-- ===================================================================
CREATE TABLE slot_holds (
    hold_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    slot_id BIGINT NOT NULL,
    patient_id INT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    expires_at DATETIME NOT NULL,
    released BOOLEAN DEFAULT FALSE,
    release_reason VARCHAR(255),
    FOREIGN KEY (slot_id) REFERENCES appointment_slots(slot_id),
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id)
);

-- ===================================================================
-- 17. DOCTOR REVIEWS
-- ===================================================================
CREATE TABLE doctor_reviews (
    review_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    doctor_id INT NOT NULL,
    patient_id INT NOT NULL,
    appointment_id INT NULL,
    rating TINYINT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment TEXT,
    is_anonymous BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id),
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id),
    FOREIGN KEY (appointment_id) REFERENCES appointments(appointment_id),
    INDEX idx_doctor_rating (doctor_id, rating)
);

-- ===================================================================
-- 18. CANCELLATIONS
-- ===================================================================
CREATE TABLE appointment_cancellations (
    cancellation_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    appointment_id INT NOT NULL,
    cancelled_by ENUM('PATIENT', 'DOCTOR', 'SYSTEM') NOT NULL,
    reason VARCHAR(255),
    custom_reason TEXT,
    cancelled_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (appointment_id) REFERENCES appointments(appointment_id)
);

-- ===================================================================
-- 19. REFRESH TOKENS & SESSIONS
-- ===================================================================
CREATE TABLE refresh_tokens (
    refresh_token_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    refresh_token_hash VARCHAR(255) NOT NULL,
    device_info VARCHAR(255),
    ip_address VARCHAR(45),
    issued_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    expires_at DATETIME NOT NULL,
    revoked BOOLEAN DEFAULT FALSE,
    revoked_at DATETIME NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    INDEX idx_refresh_expires (expires_at)
);

CREATE TABLE revoked_jwts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    jti VARCHAR(255) NOT NULL UNIQUE,
    user_id INT NULL,
    revoked_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    expires_at DATETIME NULL,
    reason VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- ===================================================================
-- 20. AUDIT LOGS (Also synced to MongoDB later)
-- ===================================================================
CREATE TABLE audit_logs (
    log_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NULL,
    action VARCHAR(255) NOT NULL,
    object_type VARCHAR(100) NULL,
    object_id BIGINT NULL,
    details TEXT,
    ip_address VARCHAR(45) NULL,
    user_agent VARCHAR(255) NULL,
    log_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    INDEX idx_audit_user (user_id, log_date),
    INDEX idx_audit_action (action)
);

-- ===================================================================
-- 21. ANALYTICS: DAILY SUMMARY (Pre-aggregated for MVP Dashboard)
-- ===================================================================
CREATE TABLE analytics_daily_summary (
    summary_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    entity_type ENUM('DOCTOR', 'HOSPITAL', 'PLATFORM') NOT NULL,
    entity_id INT NOT NULL,
    summary_date DATE NOT NULL,
    appointments_booked INT DEFAULT 0,
    appointments_completed INT DEFAULT 0,
    appointments_cancelled INT DEFAULT 0,
    appointments_no_show INT DEFAULT 0,
    revenue_generated DECIMAL(12,2) DEFAULT 0.00,
    revenue_refunded DECIMAL(12,2) DEFAULT 0.00,
    new_patients INT DEFAULT 0,
    returning_patients INT DEFAULT 0,
    avg_rating DECIMAL(3,2) DEFAULT 0.00,
    total_reviews INT DEFAULT 0,
    symptom_queries INT DEFAULT 0,
    generated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_entity_date (entity_type, entity_id, summary_date),
    INDEX idx_date (summary_date),
    INDEX idx_entity (entity_type, entity_id)
);

-- ===================================================================
-- 22. INDEXES (Performance Boost)
-- ===================================================================
CREATE INDEX idx_appointments_patient_status ON appointments (patient_id, status, appointment_date);
CREATE INDEX idx_appointments_doctor_date ON appointments (doctor_id, appointment_date);
CREATE INDEX idx_appointment_slot ON appointments (slot_id);
CREATE INDEX idx_record_appointment ON medical_records (appointment_id);
CREATE INDEX idx_chat_appointment ON chat_messages (appointment_id);
CREATE INDEX idx_invoice_status ON invoices (payment_status);

-- ===================================================================
-- 23. SEED DATA (Roles + Demo Hospital)
-- ===================================================================
INSERT INTO roles (name) VALUES ('ADMIN'), ('DOCTOR'), ('PATIENT');

-- Optional: Seed a hospital
INSERT INTO hospitals (name, type, address, timezone) 
VALUES ('City General Hospital', 'multispecialty', '123 Main St, Mumbai', 'Asia/Kolkata');

-- ===================================================================
-- DONE!
-- ===================================================================
SELECT 'Docease MVP Database Created Successfully!' AS Status;

SELECT * FROM docease_microservice.hospitals;

SHOW TABLES;

SELECT * FROM docease_microservice.roles;
SELECT * FROM docease_microservice.doctors;
SELECT * FROM docease_microservice.users;
SELECT * FROM docease_microservice.hospitals;

DESCRIBE users;

-- Check if constraint exists
SHOW CREATE TABLE users;




