package com.retailflow.inventory.client;

import com.retailflow.inventory.dto.ProductResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign Client for Product Service.
 *
 * name = "product"  →  matches spring.application.name in product-service.
 * Eureka resolves the actual host:port at runtime.
 *
 * Used in InventoryService to validate a product exists
 * before creating an inventory record.
 */
@FeignClient(name = "product", url="${product.service.url}")
public interface ProductClient {

    @GetMapping("/api/products/{id}")
    ProductResponseDTO getProductById(@PathVariable("id") Long id);
}
