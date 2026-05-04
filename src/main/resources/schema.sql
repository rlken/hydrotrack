-- ============================================================
-- HydroTrack Database Schema
-- Water Fountain Cleanliness Monitoring System
-- SDG 6: Clean Water and Sanitation | NU Manila Main Building
-- ============================================================

CREATE DATABASE IF NOT EXISTS hydrotrack;
USE hydrotrack;

-- ======================== TABLES ========================

CREATE TABLE IF NOT EXISTS roles (
    role_id INT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(30) NOT NULL UNIQUE,
    description VARCHAR(255)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS user_roles (
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS buildings (
    building_id INT AUTO_INCREMENT PRIMARY KEY,
    building_name VARCHAR(100) NOT NULL,
    building_code VARCHAR(20) NOT NULL UNIQUE,
    address VARCHAR(255),
    total_floors INT,
    latitude DECIMAL(10,7),
    longitude DECIMAL(10,7)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS fountains (
    fountain_id INT AUTO_INCREMENT PRIMARY KEY,
    building_id INT NOT NULL,
    fountain_code VARCHAR(20) NOT NULL UNIQUE,
    fountain_type VARCHAR(30) NOT NULL,
    floor_number INT NOT NULL,
    location_description VARCHAR(255),
    status VARCHAR(20) NOT NULL DEFAULT 'OPERATIONAL',
    installation_date DATE,
    last_filter_change DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (building_id) REFERENCES buildings(building_id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS water_quality_records (
    record_id INT AUTO_INCREMENT PRIMARY KEY,
    fountain_id INT NOT NULL,
    recorded_by INT NOT NULL,
    ph_level DECIMAL(4,2),
    turbidity DECIMAL(6,2),
    tds DECIMAL(7,2),
    temperature DECIMAL(5,2),
    cleanliness_rating VARCHAR(20) NOT NULL,
    notes TEXT,
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (fountain_id) REFERENCES fountains(fountain_id) ON DELETE CASCADE,
    FOREIGN KEY (recorded_by) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS filter_schedules (
    schedule_id INT AUTO_INCREMENT PRIMARY KEY,
    fountain_id INT NOT NULL,
    filter_type VARCHAR(50) NOT NULL,
    replacement_interval_days INT NOT NULL,
    next_replacement_date DATE NOT NULL,
    priority VARCHAR(20) NOT NULL DEFAULT 'NORMAL',
    status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED',
    FOREIGN KEY (fountain_id) REFERENCES fountains(fountain_id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS maintenance_logs (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    fountain_id INT NOT NULL,
    performed_by INT NOT NULL,
    schedule_id INT,
    maintenance_type VARCHAR(30) NOT NULL,
    description TEXT,
    parts_replaced VARCHAR(255),
    cost DECIMAL(10,2),
    performed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (fountain_id) REFERENCES fountains(fountain_id) ON DELETE CASCADE,
    FOREIGN KEY (performed_by) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (schedule_id) REFERENCES filter_schedules(schedule_id) ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS audit_logs (
    audit_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    action_type VARCHAR(20) NOT NULL,
    table_name VARCHAR(50) NOT NULL,
    record_id INT,
    old_values TEXT,
    new_values TEXT,
    action_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- ======================== INDEXES ========================

CREATE INDEX idx_fountain_building ON fountains(building_id) USING BTREE;
CREATE INDEX idx_fountain_floor ON fountains(floor_number) USING BTREE;
CREATE INDEX idx_fountain_status ON fountains(status) USING BTREE;
CREATE INDEX idx_wqr_fountain_date ON water_quality_records(fountain_id, recorded_at) USING BTREE;
CREATE INDEX idx_wqr_rating ON water_quality_records(cleanliness_rating) USING BTREE;
CREATE INDEX idx_filter_next_date ON filter_schedules(next_replacement_date) USING BTREE;
CREATE INDEX idx_filter_status ON filter_schedules(status) USING BTREE;
CREATE INDEX idx_maint_date ON maintenance_logs(performed_at) USING BTREE;
CREATE INDEX idx_audit_action_at ON audit_logs(action_at) USING BTREE;
