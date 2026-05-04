package com.hydrotrack.repository;

import com.hydrotrack.model.FilterSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface FilterScheduleRepository extends JpaRepository<FilterSchedule, Long> {

    List<FilterSchedule> findByFountainFountainId(Long fountainId);

    List<FilterSchedule> findByStatus(String status);

    @Query("SELECT fs FROM FilterSchedule fs WHERE fs.nextReplacementDate <= :date AND fs.status = 'SCHEDULED'")
    List<FilterSchedule> findOverdueSchedules(@Param("date") LocalDate date);

    @Query("SELECT fs FROM FilterSchedule fs WHERE fs.nextReplacementDate BETWEEN :start AND :end AND fs.status = 'SCHEDULED' ORDER BY fs.nextReplacementDate ASC")
    List<FilterSchedule> findUpcomingSchedules(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT COUNT(fs) FROM FilterSchedule fs WHERE fs.status = 'OVERDUE'")
    long countOverdue();

    @Query("SELECT fs FROM FilterSchedule fs WHERE fs.status IN ('SCHEDULED', 'OVERDUE') ORDER BY fs.nextReplacementDate ASC")
    List<FilterSchedule> findActiveSchedules();
}
