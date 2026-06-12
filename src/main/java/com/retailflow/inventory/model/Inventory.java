package com.retailflow.inventory.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.Objects;

@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inventoryId;

    /*
     * productId stored as a plain Long column.
     * Product details are fetched from product-service via Feign — no JPA join needed.
     * Column name kept as "productId" to match the existing DB schema.
     */
    @Column(name = "productId")
    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Location ID is required")
    private Long locationId;

    @Min(value = 0, message = "Quantity on hand cannot be negative")
    private Integer quantityOnHand;

    @Min(value = 0, message = "Safety stock cannot be negative")
    private Integer safetyStock;

    private String status;

    public Inventory() {}

    public Long getInventoryId() { return inventoryId; }
    public void setInventoryId(Long inventoryId) { this.inventoryId = inventoryId; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Long getLocationId() { return locationId; }
    public void setLocationId(Long locationId) { this.locationId = locationId; }
    public Integer getQuantityOnHand() { return quantityOnHand; }
    public void setQuantityOnHand(Integer quantityOnHand) { this.quantityOnHand = quantityOnHand; }
    public Integer getSafetyStock() { return safetyStock; }
    public void setSafetyStock(Integer safetyStock) { this.safetyStock = safetyStock; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Inventory inventory = (Inventory) o;
        return Objects.equals(inventoryId, inventory.inventoryId);
    }

    @Override
    public int hashCode() { return Objects.hash(inventoryId); }
}
