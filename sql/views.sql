-- ============================================================
-- HydroTrack — Advanced Database Features
-- Views, Stored Procedures, Functions, Triggers
-- ============================================================

-- ======================== VIEWS ========================

-- View 1: Fountain Status Summary
CREATE OR REPLACE VIEW vw_fountain_status_summary AS
SELECT f.fountain_id, f.fountain_code, f.fountain_type, f.floor_number,
    f.location_description, f.status AS fountain_status, f.last_filter_change,
    b.building_name, wq.ph_level AS latest_ph, wq.turbidity AS latest_turbidity,
    wq.tds AS latest_tds, wq.cleanliness_rating AS latest_rating,
    wq.recorded_at AS last_quality_check
FROM fountains f
JOIN buildings b ON f.building_id = b.building_id
LEFT JOIN water_quality_records wq ON wq.record_id = (
    SELECT record_id FROM water_quality_records
    WHERE fountain_id = f.fountain_id ORDER BY recorded_at DESC LIMIT 1
);

-- View 2: Overdue Filters
CREATE OR REPLACE VIEW vw_overdue_filters AS
SELECT fs.schedule_id, f.fountain_code, f.floor_number, f.location_description,
    fs.filter_type, fs.next_replacement_date,
    DATEDIFF(CURDATE(), fs.next_replacement_date) AS days_overdue, fs.priority
FROM filter_schedules fs
JOIN fountains f ON fs.fountain_id = f.fountain_id
WHERE fs.next_replacement_date < CURDATE() AND fs.status IN ('SCHEDULED','OVERDUE')
ORDER BY days_overdue DESC;

-- View 3: Water Quality Trends (monthly averages)
CREATE OR REPLACE VIEW vw_water_quality_trends AS
SELECT f.fountain_id, f.fountain_code, f.floor_number,
    YEAR(wq.recorded_at) AS record_year, MONTH(wq.recorded_at) AS record_month,
    COUNT(*) AS total_readings,
    ROUND(AVG(wq.ph_level),2) AS avg_ph, ROUND(AVG(wq.turbidity),2) AS avg_turbidity,
    ROUND(AVG(wq.tds),2) AS avg_tds, ROUND(AVG(wq.temperature),2) AS avg_temperature
FROM water_quality_records wq
JOIN fountains f ON wq.fountain_id = f.fountain_id
GROUP BY f.fountain_id, f.fountain_code, f.floor_number, YEAR(wq.recorded_at), MONTH(wq.recorded_at);

-- View 4: Maintenance History
CREATE OR REPLACE VIEW vw_maintenance_history AS
SELECT ml.log_id, f.fountain_code, f.floor_number, u.full_name AS performed_by_name,
    ml.maintenance_type, ml.description, ml.parts_replaced, ml.cost, ml.performed_at
FROM maintenance_logs ml
JOIN fountains f ON ml.fountain_id = f.fountain_id
JOIN users u ON ml.performed_by = u.user_id
ORDER BY ml.performed_at DESC;

-- View 5: Floor Health Scores
CREATE OR REPLACE VIEW vw_floor_health_scores AS
SELECT f.floor_number, COUNT(DISTINCT f.fountain_id) AS total_fountains,
    SUM(CASE WHEN f.status='OPERATIONAL' THEN 1 ELSE 0 END) AS operational_count,
    ROUND((SUM(CASE WHEN f.status='OPERATIONAL' THEN 1 ELSE 0 END)*100.0)/COUNT(DISTINCT f.fountain_id),1) AS health_pct
FROM fountains f GROUP BY f.floor_number ORDER BY f.floor_number;
