package com.retailflow.inventory.dto;

import jakarta.validation.constraints.*;

public class InventoryRequestDTO {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Location ID is required")
    private Long locationId;

    @Min(value = 0, message = "Quantity on hand cannot be negative")
    private Integer quantityOnHand;

    @Min(value = 0, message = "Safety stock cannot be negative")
    private Integer safetyStock;

    private String status;

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
}
