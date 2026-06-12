package com.retailflow.inventory.dto;

public class InventoryResponseDTO {

    private Long inventoryId;
    private Long productId;
    private String productName;
    private Long locationId;
    private Integer quantityOnHand;
    private Integer safetyStock;
    private String status;
    private boolean lowStock;

    public Long getInventoryId() { return inventoryId; }
    public void setInventoryId(Long inventoryId) { this.inventoryId = inventoryId; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public Long getLocationId() { return locationId; }
    public void setLocationId(Long locationId) { this.locationId = locationId; }
    public Integer getQuantityOnHand() { return quantityOnHand; }
    public void setQuantityOnHand(Integer quantityOnHand) { this.quantityOnHand = quantityOnHand; }
    public Integer getSafetyStock() { return safetyStock; }
    public void setSafetyStock(Integer safetyStock) { this.safetyStock = safetyStock; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public boolean isLowStock() { return lowStock; }
    public void setLowStock(boolean lowStock) { this.lowStock = lowStock; }
}
