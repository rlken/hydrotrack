-- ============================================================
-- HydroTrack — Seed Data (NU Manila Main Building)
-- ============================================================

-- Roles
INSERT IGNORE INTO roles (role_name, description) VALUES
('ADMIN', 'Full system access'),
('TECHNICIAN', 'Manage fountains, schedules, and maintenance'),
('INSPECTOR', 'Record and view water quality'),
('VIEWER', 'View-only access to dashboard and data');

-- Default Admin (password: admin123)
INSERT IGNORE INTO users (username, email, password_hash, full_name) VALUES
('admin', 'admin@nu.edu.ph', '$2a$10$1P98GXYF29L8M77k9/K9xezQWv3p4e4w3z3y9A6e34H8m3m2l1O2K', 'System Administrator');

INSERT IGNORE INTO user_roles (user_id, role_id) VALUES (1, 1);

-- Sample users (all passwords: password123)
INSERT IGNORE INTO users (username, email, password_hash, full_name) VALUES
('tech.juan', 'juan@nu.edu.ph', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Juan Dela Cruz'),
('insp.maria', 'maria@nu.edu.ph', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Maria Santos'),
('viewer.jose', 'jose@nu.edu.ph', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Jose Rizal');

INSERT IGNORE INTO user_roles (user_id, role_id) VALUES (2, 2), (3, 3), (4, 4);

-- NU Manila Main Building
INSERT IGNORE INTO buildings (building_name, building_code, address, total_floors, latitude, longitude) VALUES
('NU Manila Main Building', 'NU-MAIN', '551 M.F. Jhocson St, Sampaloc, Manila', 8, 14.6037, 120.9887);

-- Fountains (across 8 floors)
INSERT IGNORE INTO fountains (building_id, fountain_code, fountain_type, floor_number, location_description, status, installation_date, last_filter_change) VALUES
(1, 'NM-G01', 'WATER_DISPENSER', 1, 'Ground Floor Lobby, near Guard Station', 'OPERATIONAL', '2024-01-15', '2025-03-01'),
(1, 'NM-G02', 'DRINKING_FOUNTAIN', 1, 'Ground Floor Hallway, East Wing', 'OPERATIONAL', '2024-01-15', '2025-02-15'),
(1, 'NM-201', 'WATER_DISPENSER', 2, '2nd Floor, near Room 201', 'OPERATIONAL', '2024-02-01', '2025-03-10'),
(1, 'NM-202', 'BOTTLE_REFILL', 2, '2nd Floor, Student Lounge', 'NEEDS_MAINTENANCE', '2024-02-01', '2025-01-20'),
(1, 'NM-301', 'WATER_DISPENSER', 3, '3rd Floor, CS Lab Area', 'OPERATIONAL', '2024-03-01', '2025-04-01'),
(1, 'NM-302', 'DRINKING_FOUNTAIN', 3, '3rd Floor, near Elevator', 'OPERATIONAL', '2024-03-01', '2025-03-15'),
(1, 'NM-401', 'WATER_DISPENSER', 4, '4th Floor, Faculty Lounge', 'OPERATIONAL', '2024-04-01', '2025-03-20'),
(1, 'NM-501', 'BOTTLE_REFILL', 5, '5th Floor, Library Entrance', 'OUT_OF_SERVICE', '2024-01-20', '2024-12-01'),
(1, 'NM-601', 'WATER_DISPENSER', 6, '6th Floor, near Room 601', 'OPERATIONAL', '2024-05-01', '2025-04-05'),
(1, 'NM-701', 'DRINKING_FOUNTAIN', 7, '7th Floor, Auditorium Lobby', 'UNDER_REPAIR', '2024-06-01', '2025-02-01'),
(1, 'NM-801', 'WATER_DISPENSER', 8, '8th Floor, Admin Office', 'OPERATIONAL', '2024-06-15', '2025-04-10');

-- Water Quality Records
INSERT IGNORE INTO water_quality_records (fountain_id, recorded_by, ph_level, turbidity, tds, temperature, cleanliness_rating, notes) VALUES
(1, 3, 7.20, 0.50, 120.00, 24.5, 'EXCELLENT', 'Crystal clear water'),
(2, 3, 7.00, 0.80, 150.00, 25.0, 'GOOD', 'Normal quality'),
(3, 3, 6.80, 1.20, 200.00, 23.8, 'GOOD', 'Slightly below avg pH'),
(4, 3, 6.50, 3.50, 350.00, 26.0, 'POOR', 'High turbidity, needs filter change'),
(5, 3, 7.10, 0.60, 130.00, 24.0, 'EXCELLENT', 'Very clean'),
(6, 3, 7.30, 0.90, 160.00, 24.2, 'GOOD', 'Normal'),
(7, 3, 7.00, 0.70, 140.00, 23.5, 'EXCELLENT', 'Faculty area well maintained'),
(8, 3, 5.80, 5.00, 500.00, 28.0, 'CRITICAL', 'Unit out of service - awaiting repair'),
(9, 3, 7.15, 0.55, 125.00, 24.0, 'EXCELLENT', 'Good condition'),
(10, 3, 6.90, 2.00, 250.00, 25.5, 'FAIR', 'Under repair'),
(11, 3, 7.05, 0.65, 135.00, 23.8, 'GOOD', 'Admin floor maintained');

-- Filter Schedules
INSERT IGNORE INTO filter_schedules (fountain_id, filter_type, replacement_interval_days, next_replacement_date, priority, status) VALUES
(1, 'CARBON', 90, '2025-06-01', 'NORMAL', 'SCHEDULED'),
(2, 'SEDIMENT', 60, '2025-04-15', 'HIGH', 'OVERDUE'),
(3, 'CARBON', 90, '2025-06-10', 'NORMAL', 'SCHEDULED'),
(4, 'REVERSE_OSMOSIS', 180, '2025-04-20', 'URGENT', 'OVERDUE'),
(5, 'CARBON', 90, '2025-07-01', 'NORMAL', 'SCHEDULED'),
(6, 'SEDIMENT', 60, '2025-05-15', 'NORMAL', 'SCHEDULED'),
(7, 'UV', 365, '2026-03-20', 'LOW', 'SCHEDULED'),
(8, 'REVERSE_OSMOSIS', 180, '2025-06-01', 'URGENT', 'SCHEDULED'),
(9, 'CARBON', 90, '2025-07-05', 'NORMAL', 'SCHEDULED'),
(11, 'SEDIMENT', 60, '2025-06-10', 'NORMAL', 'SCHEDULED');

-- Maintenance Logs
INSERT IGNORE INTO maintenance_logs (fountain_id, performed_by, schedule_id, maintenance_type, description, parts_replaced, cost) VALUES
(1, 2, NULL, 'CLEANING', 'Routine cleaning of dispenser nozzle and basin', NULL, 150.00),
(1, 2, NULL, 'FILTER_CHANGE', 'Replaced carbon filter', 'Carbon Filter CF-200', 850.00),
(4, 2, NULL, 'INSPECTION', 'Inspected unit - filter severely clogged', NULL, 0.00),
(5, 2, NULL, 'CLEANING', 'Deep cleaning of CS lab dispenser', NULL, 200.00),
(7, 2, NULL, 'FILTER_CHANGE', 'UV lamp replacement', 'UV Lamp UL-100', 1200.00),
(10, 2, NULL, 'REPAIR', 'Replaced broken faucet handle', 'Faucet Handle FH-50', 500.00);
