package com.retailflow.inventory.dto;

/**
 * DTO sent to auditlog-service via AuditLogClient (Feign).
 * Mirrors com.retailflow.auditlog.dto.AuditLogRequestDTO.
 */
public class AuditLogRequestDTO {

    private String action;
    private String entityType;
    private Long entityId;
    private String description;
    private String status;

    public AuditLogRequestDTO() {}

    public AuditLogRequestDTO(String action, String entityType, Long entityId,
                               String description, String status) {
        this.action = action;
        this.entityType = entityType;
        this.entityId = entityId;
        this.description = description;
        this.status = status;
    }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getEntityType() { return entityType; }
    public void setEntityType(String entityType) { this.entityType = entityType; }
    public Long getEntityId() { return entityId; }
    public void setEntityId(Long entityId) { this.entityId = entityId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
