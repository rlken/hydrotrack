package com.hydrotrack.repository;

import com.hydrotrack.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByTableName(String tableName);

    @Query("SELECT a FROM AuditLog a ORDER BY a.actionAt DESC")
    List<AuditLog> findAllOrderByActionAtDesc();

    List<AuditLog> findByUserUserId(Long userId);
}
