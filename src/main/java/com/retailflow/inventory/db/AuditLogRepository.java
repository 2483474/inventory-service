package com.retailflow.inventory.db;

import com.retailflow.inventory.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByEntityType(String entityType);
    List<AuditLog> findByEntityId(Long entityId);
    List<AuditLog> findByStatus(String status);
    List<AuditLog> findByAction(String action);
}
