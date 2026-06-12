package com.retailflow.inventory.client;

import com.retailflow.inventory.dto.AuditLogRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "AuditLogRepository",url="${auditlog.service.url}")
public interface AuditLogClient {

    @PostMapping("/api/audit-logs")
    void save(@RequestBody AuditLogRequestDTO dto);
}
