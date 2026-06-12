package com.retailflow.inventory.controller;

import com.retailflow.inventory.dto.InventoryRequestDTO;
import com.retailflow.inventory.dto.InventoryResponseDTO;
import com.retailflow.inventory.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<InventoryResponseDTO> addInventory(
            @Valid @RequestBody InventoryRequestDTO dto) {
        return ResponseEntity.ok(inventoryService.addInventory(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryResponseDTO> updateInventory(
            @PathVariable Long id,
            @Valid @RequestBody InventoryRequestDTO dto) {
        return ResponseEntity.ok(inventoryService.updateInventory(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Long id) {
        inventoryService.deleteInventory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryResponseDTO> getInventory(@PathVariable Long id) {
        return ResponseEntity.ok(inventoryService.getInventoryById(id));
    }

    @GetMapping
    public ResponseEntity<List<InventoryResponseDTO>> getAllInventory() {
        return ResponseEntity.ok(inventoryService.getAllInventory());
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<InventoryResponseDTO>> getByProduct(
            @PathVariable Long productId) {
        return ResponseEntity.ok(inventoryService.getInventoryByProduct(productId));
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<InventoryResponseDTO>> getLowStock() {
        return ResponseEntity.ok(inventoryService.getLowStockInventory());
    }

    // Called by PurchaseOrder Service via Feign when a PO is delivered
    @PatchMapping("/{id}/replenish")
    public ResponseEntity<InventoryResponseDTO> replenish(
            @PathVariable Long id,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(inventoryService.replenishStock(id, quantity));
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<InventoryResponseDTO>> getPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(inventoryService.getAllInventoryWithPagination(page, size));
    }
}
