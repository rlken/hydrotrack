-- ============================================================
-- HydroTrack — Triggers
-- ============================================================

DELIMITER //

-- Trigger 1: Audit log on water quality INSERT
CREATE TRIGGER trg_audit_wq_insert AFTER INSERT ON water_quality_records
FOR EACH ROW
BEGIN
    INSERT INTO audit_logs (user_id,action_type,table_name,record_id,new_values)
    VALUES (NEW.recorded_by,'INSERT','water_quality_records',NEW.record_id,
        CONCAT('pH:',IFNULL(NEW.ph_level,'N/A'),' Rating:',NEW.cleanliness_rating));
END //

-- Trigger 2: Audit log on fountain UPDATE
CREATE TRIGGER trg_audit_fountain_update AFTER UPDATE ON fountains
FOR EACH ROW
BEGIN
    IF OLD.status != NEW.status THEN
        INSERT INTO audit_logs (action_type,table_name,record_id,old_values,new_values)
        VALUES ('UPDATE','fountains',NEW.fountain_id,
            CONCAT('Status:',OLD.status), CONCAT('Status:',NEW.status));
    END IF;
END //

-- Trigger 3: Validate pH range
CREATE TRIGGER trg_validate_ph BEFORE INSERT ON water_quality_records
FOR EACH ROW
BEGIN
    IF NEW.ph_level IS NOT NULL AND (NEW.ph_level < 0 OR NEW.ph_level > 14) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'pH level must be between 0 and 14';
    END IF;
END //

-- Trigger 4: Auto-update fountain status on poor quality
CREATE TRIGGER trg_auto_fountain_status AFTER INSERT ON water_quality_records
FOR EACH ROW
BEGIN
    IF NEW.cleanliness_rating IN ('CRITICAL','POOR') THEN
        UPDATE fountains SET status='NEEDS_MAINTENANCE' WHERE fountain_id=NEW.fountain_id;
    END IF;
END //

-- Trigger 5: Auto-mark overdue schedules
CREATE TRIGGER trg_filter_overdue BEFORE UPDATE ON filter_schedules
FOR EACH ROW
BEGIN
    IF NEW.status='SCHEDULED' AND NEW.next_replacement_date < CURDATE() THEN
        SET NEW.status='OVERDUE';
        SET NEW.priority='URGENT';
    END IF;
END //

DELIMITER ;
