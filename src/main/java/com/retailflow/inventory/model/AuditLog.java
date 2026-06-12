package com.retailflow.inventory.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // What was done: CREATE, UPDATE, DELETE, REPLENISH
    private String action;

    // Which domain: always "INVENTORY" in this service
    private String entityType;

    // ID of the affected inventory record (null on failures before save)
    private Long entityId;

    // Human-readable description of what happened
    @Column(length = 500)
    private String description;

    // SUCCESS or FAILURE
    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public AuditLog() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
