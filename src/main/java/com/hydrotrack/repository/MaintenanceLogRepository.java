package com.hydrotrack.repository;

import com.hydrotrack.model.MaintenanceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MaintenanceLogRepository extends JpaRepository<MaintenanceLog, Long> {

    List<MaintenanceLog> findByFountainFountainIdOrderByPerformedAtDesc(Long fountainId);

    List<MaintenanceLog> findByMaintenanceType(String type);

    @Query("SELECT m FROM MaintenanceLog m ORDER BY m.performedAt DESC")
    List<MaintenanceLog> findAllOrderByPerformedAtDesc();

    @Query("SELECT m FROM MaintenanceLog m WHERE m.performedBy.userId = :userId ORDER BY m.performedAt DESC")
    List<MaintenanceLog> findByPerformedByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(m) FROM MaintenanceLog m WHERE MONTH(m.performedAt) = :month AND YEAR(m.performedAt) = :year")
    long countByMonth(@Param("month") int month, @Param("year") int year);
}
