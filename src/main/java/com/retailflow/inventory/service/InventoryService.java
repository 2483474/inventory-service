package com.retailflow.inventory.service;

import com.retailflow.inventory.client.AuditLogClient;
import com.retailflow.inventory.client.ProductClient;
import com.retailflow.inventory.db.InventoryRepository;
import com.retailflow.inventory.dto.AuditLogRequestDTO;
import com.retailflow.inventory.dto.InventoryRequestDTO;
import com.retailflow.inventory.dto.InventoryResponseDTO;
import com.retailflow.inventory.dto.ProductResponseDTO;
import com.retailflow.inventory.exception.BadRequestException;
import com.retailflow.inventory.exception.ResourceNotFoundException;
import com.retailflow.inventory.model.Inventory;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private static final String ENTITY = "INVENTORY";

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductClient productClient;

    /** Feign → auditlog-service. Calls are best-effort; wrapped in try-catch. */
    @Autowired
    private AuditLogClient auditLogClient;

    // ─── Create ────────────────────────────────────────────────────────────────

    public InventoryResponseDTO addInventory(InventoryRequestDTO dto) {
        try {
            ProductResponseDTO product = fetchProductOrThrow(dto.getProductId());

            inventoryRepository
                    .findByProductIdAndLocationId(dto.getProductId(), dto.getLocationId())
                    .ifPresent(i -> {
                        throw new BadRequestException(
                                "Inventory already exists for this product at this location");
                    });

            Inventory inventory = new Inventory();
            inventory.setProductId(product.getProductId());
            inventory.setLocationId(dto.getLocationId());
            inventory.setQuantityOnHand(dto.getQuantityOnHand());
            inventory.setSafetyStock(dto.getSafetyStock());
            inventory.setStatus("ACTIVE");

            InventoryResponseDTO result = mapToDTO(inventoryRepository.save(inventory));

            audit("CREATE", ENTITY, result.getInventoryId(),
                    "Created inventory for product ID: " + dto.getProductId()
                            + " | Location: " + dto.getLocationId()
                            + " | Qty: " + dto.getQuantityOnHand()
                            + " | Safety stock: " + dto.getSafetyStock());
            return result;

        } catch (BadRequestException | ResourceNotFoundException e) {
            auditFailure("CREATE", ENTITY,
                    "Failed to create inventory for product ID: " + dto.getProductId()
                            + " | Reason: " + e.getMessage());
            throw e;
        }
    }

    // ─── Update ────────────────────────────────────────────────────────────────

    public InventoryResponseDTO updateInventory(Long id, InventoryRequestDTO dto) {
        try {
            Inventory inventory = inventoryRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Inventory not found with ID: " + id));

            inventory.setQuantityOnHand(dto.getQuantityOnHand());
            inventory.setSafetyStock(dto.getSafetyStock());
            inventory.setLocationId(dto.getLocationId());
            inventory.setStatus(dto.getStatus());

            InventoryResponseDTO result = mapToDTO(inventoryRepository.save(inventory));

            audit("UPDATE", ENTITY, id,
                    "Updated inventory ID: " + id
                            + " | Qty: " + dto.getQuantityOnHand()
                            + " | Safety stock: " + dto.getSafetyStock()
                            + " | Status: " + dto.getStatus());
            return result;

        } catch (ResourceNotFoundException e) {
            auditFailure("UPDATE", ENTITY,
                    "Failed to update inventory ID: " + id + " | Reason: " + e.getMessage());
            throw e;
        }
    }

    // ─── Soft Delete ───────────────────────────────────────────────────────────

    public void deleteInventory(Long id) {
        try {
            Inventory inventory = inventoryRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Inventory not found with ID: " + id));

            inventory.setStatus("INACTIVE");
            inventoryRepository.save(inventory);

            audit("DELETE", ENTITY, id,
                    "Soft-deleted inventory ID: " + id + " | Status set to INACTIVE");

        } catch (ResourceNotFoundException e) {
            auditFailure("DELETE", ENTITY,
                    "Failed to delete inventory ID: " + id + " | Reason: " + e.getMessage());
            throw e;
        }
    }

    // ─── Replenish Stock ───────────────────────────────────────────────────────

    public InventoryResponseDTO replenishStock(Long id, Integer quantity) {
        try {
            if (quantity <= 0) throw new BadRequestException("Quantity must be greater than 0");

            Inventory inventory = inventoryRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Inventory not found with ID: " + id));

            int previousQty = inventory.getQuantityOnHand();
            inventory.setQuantityOnHand(previousQty + quantity);
            InventoryResponseDTO result = mapToDTO(inventoryRepository.save(inventory));

            audit("REPLENISH", ENTITY, id,
                    "Replenished inventory ID: " + id
                            + " | Added: " + quantity
                            + " | Previous qty: " + previousQty
                            + " | New qty: " + result.getQuantityOnHand());
            return result;

        } catch (BadRequestException | ResourceNotFoundException e) {
            auditFailure("REPLENISH", ENTITY,
                    "Failed to replenish inventory ID: " + id + " | Reason: " + e.getMessage());
            throw e;
        }
    }

    // ─── Read ──────────────────────────────────────────────────────────────────

    public InventoryResponseDTO getInventoryById(Long id) {
        return mapToDTO(inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory not found with ID: " + id)));
    }

    public List<InventoryResponseDTO> getAllInventory() {
        return inventoryRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<InventoryResponseDTO> getInventoryByProduct(Long productId) {
        return inventoryRepository.findByProductId(productId).stream()
                .map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<InventoryResponseDTO> getLowStockInventory() {
        return inventoryRepository.findAll().stream()
                .filter(i -> i.getQuantityOnHand() != null && i.getSafetyStock() != null
                        && i.getQuantityOnHand() < i.getSafetyStock())
                .map(this::mapToDTO).collect(Collectors.toList());
    }

    public Page<InventoryResponseDTO> getAllInventoryWithPagination(int page, int size) {
        return inventoryRepository.findAll(PageRequest.of(page, size)).map(this::mapToDTO);
    }

    // ─── Audit Helpers (best-effort — never fails the main operation) ──────────

    private void audit(String action, String entityType, Long entityId, String description) {
        try {
            auditLogClient.save(
                    new AuditLogRequestDTO(action, entityType, entityId, description, "SUCCESS"));
        } catch (Exception ignored) {}
    }

    private void auditFailure(String action, String entityType, String description) {
        try {
            auditLogClient.save(
                    new AuditLogRequestDTO(action, entityType, null, description, "FAILURE"));
        } catch (Exception ignored) {}
    }

    // ─── Other Helpers ─────────────────────────────────────────────────────────

    private ProductResponseDTO fetchProductOrThrow(Long productId) {
        try {
            return productClient.getProductById(productId);
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("Product not found with ID: " + productId);
        } catch (FeignException e) {
            throw new RuntimeException("Failed to reach product-service: " + e.getMessage());
        }
    }

    private InventoryResponseDTO mapToDTO(Inventory i) {
        InventoryResponseDTO dto = new InventoryResponseDTO();
        dto.setInventoryId(i.getInventoryId());
        dto.setProductId(i.getProductId());
        dto.setLocationId(i.getLocationId());
        dto.setQuantityOnHand(i.getQuantityOnHand());
        dto.setSafetyStock(i.getSafetyStock());
        dto.setStatus(i.getStatus());
        try {
            dto.setProductName(productClient.getProductById(i.getProductId()).getProductName());
        } catch (Exception e) {
            dto.setProductName("N/A");
        }
        if (i.getQuantityOnHand() != null && i.getSafetyStock() != null)
            dto.setLowStock(i.getQuantityOnHand() < i.getSafetyStock());
        return dto;
    }
}
