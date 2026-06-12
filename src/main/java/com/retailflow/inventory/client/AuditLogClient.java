package com.retailflow.inventory.client;

import com.retailflow.inventory.dto.AuditLogRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Feign Client for AuditLog Service.
 *
 * name = "auditlog-service"  →  matches spring.application.name in auditlog-service.
 * Eureka resolves the actual host:port at runtime.
 *
 * Audit logging is best-effort — calls are wrapped in try-catch in InventoryService
 * so that a temporary auditlog-service outage never fails the main operation.
 */
@FeignClient(name = "AuditLogRepository",url="${auditlog.service.url}")
public interface AuditLogClient {

    @PostMapping("/api/audit-logs")
    void save(@RequestBody AuditLogRequestDTO dto);
}
