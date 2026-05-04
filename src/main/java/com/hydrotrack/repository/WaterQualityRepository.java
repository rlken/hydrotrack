package com.hydrotrack.repository;

import com.hydrotrack.model.WaterQualityRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WaterQualityRepository extends JpaRepository<WaterQualityRecord, Long> {

    List<WaterQualityRecord> findByFountainFountainIdOrderByRecordedAtDesc(Long fountainId);

    List<WaterQualityRecord> findByCleanlinessRating(String rating);

    @Query("SELECT w FROM WaterQualityRecord w WHERE w.fountain.fountainId = :fountainId ORDER BY w.recordedAt DESC LIMIT 1")
    WaterQualityRecord findLatestByFountainId(@Param("fountainId") Long fountainId);

    @Query("SELECT w FROM WaterQualityRecord w WHERE w.recordedAt BETWEEN :start AND :end ORDER BY w.recordedAt DESC")
    List<WaterQualityRecord> findByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT w FROM WaterQualityRecord w ORDER BY w.recordedAt DESC")
    List<WaterQualityRecord> findAllOrderByRecordedAtDesc();

    @Query("SELECT COUNT(w) FROM WaterQualityRecord w WHERE w.cleanlinessRating = :rating")
    long countByRating(@Param("rating") String rating);

    @Query("SELECT AVG(w.phLevel) FROM WaterQualityRecord w WHERE w.fountain.fountainId = :fountainId")
    Double findAveragePhByFountainId(@Param("fountainId") Long fountainId);
}
