-- ============================================================
-- HydroTrack — Stored Procedures & Functions
-- ============================================================

DELIMITER //

-- SP 1: Record water quality with validation
CREATE PROCEDURE sp_record_water_quality(
    IN p_fountain_id INT, IN p_recorded_by INT,
    IN p_ph DECIMAL(4,2), IN p_turb DECIMAL(6,2),
    IN p_tds DECIMAL(7,2), IN p_temp DECIMAL(5,2),
    IN p_rating VARCHAR(20), IN p_notes TEXT)
BEGIN
    IF p_ph IS NOT NULL AND (p_ph < 0 OR p_ph > 14) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'pH must be 0-14';
    END IF;
    INSERT INTO water_quality_records (fountain_id,recorded_by,ph_level,turbidity,tds,temperature,cleanliness_rating,notes)
    VALUES (p_fountain_id,p_recorded_by,p_ph,p_turb,p_tds,p_temp,p_rating,p_notes);
    IF p_rating IN ('CRITICAL','POOR') THEN
        UPDATE fountains SET status='NEEDS_MAINTENANCE' WHERE fountain_id=p_fountain_id;
    END IF;
END //

-- SP 2: Complete maintenance (transactional)
CREATE PROCEDURE sp_complete_maintenance(
    IN p_fid INT, IN p_uid INT, IN p_sid INT,
    IN p_type VARCHAR(30), IN p_desc TEXT,
    IN p_parts VARCHAR(255), IN p_cost DECIMAL(10,2))
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION BEGIN ROLLBACK; RESIGNAL; END;
    START TRANSACTION;
    INSERT INTO maintenance_logs (fountain_id,performed_by,schedule_id,maintenance_type,description,parts_replaced,cost)
    VALUES (p_fid,p_uid,p_sid,p_type,p_desc,p_parts,p_cost);
    IF p_sid IS NOT NULL AND p_type='FILTER_CHANGE' THEN
        UPDATE filter_schedules SET status='COMPLETED' WHERE schedule_id=p_sid;
        INSERT INTO filter_schedules (fountain_id,filter_type,replacement_interval_days,next_replacement_date,priority,status)
        SELECT fountain_id,filter_type,replacement_interval_days,DATE_ADD(CURDATE(),INTERVAL replacement_interval_days DAY),'NORMAL','SCHEDULED'
        FROM filter_schedules WHERE schedule_id=p_sid;
        UPDATE fountains SET last_filter_change=CURDATE() WHERE fountain_id=p_fid;
    END IF;
    UPDATE fountains SET status='OPERATIONAL' WHERE fountain_id=p_fid;
    COMMIT;
END //

-- SP 3: Monthly report
CREATE PROCEDURE sp_generate_monthly_report(IN p_month INT, IN p_year INT)
BEGIN
    SELECT COUNT(*) AS total_readings, ROUND(AVG(ph_level),2) AS avg_ph,
        ROUND(AVG(turbidity),2) AS avg_turb, ROUND(AVG(tds),2) AS avg_tds,
        SUM(CASE WHEN cleanliness_rating='EXCELLENT' THEN 1 ELSE 0 END) AS excellent,
        SUM(CASE WHEN cleanliness_rating='POOR' THEN 1 ELSE 0 END) AS poor,
        SUM(CASE WHEN cleanliness_rating='CRITICAL' THEN 1 ELSE 0 END) AS critical
    FROM water_quality_records WHERE MONTH(recorded_at)=p_month AND YEAR(recorded_at)=p_year;
END //

-- Function 1: Fountain health score (0-100)
CREATE FUNCTION fn_calculate_fountain_health(p_fid INT) RETURNS INT
DETERMINISTIC READS SQL DATA
BEGIN
    DECLARE v_score INT DEFAULT 100;
    DECLARE v_status VARCHAR(20);
    DECLARE v_rating VARCHAR(20);
    SELECT status INTO v_status FROM fountains WHERE fountain_id=p_fid;
    IF v_status='NEEDS_MAINTENANCE' THEN SET v_score=v_score-30;
    ELSEIF v_status='OUT_OF_SERVICE' THEN SET v_score=v_score-50;
    ELSEIF v_status='UNDER_REPAIR' THEN SET v_score=v_score-20; END IF;
    SELECT cleanliness_rating INTO v_rating FROM water_quality_records
    WHERE fountain_id=p_fid ORDER BY recorded_at DESC LIMIT 1;
    IF v_rating='CRITICAL' THEN SET v_score=v_score-40;
    ELSEIF v_rating='POOR' THEN SET v_score=v_score-20;
    ELSEIF v_rating='FAIR' THEN SET v_score=v_score-10; END IF;
    IF v_score<0 THEN SET v_score=0; END IF;
    RETURN v_score;
END //

-- Function 2: Days until filter change
CREATE FUNCTION fn_days_until_filter_change(p_fid INT) RETURNS INT
DETERMINISTIC READS SQL DATA
BEGIN
    DECLARE v_days INT;
    SELECT DATEDIFF(MIN(next_replacement_date),CURDATE()) INTO v_days
    FROM filter_schedules WHERE fountain_id=p_fid AND status IN ('SCHEDULED','OVERDUE');
    RETURN IFNULL(v_days,-1);
END //

DELIMITER ;
