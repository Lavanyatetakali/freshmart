package com.grocerystore.controller;
import com.grocerystore.dto.*;
import com.grocerystore.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> getAll(
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="12") int size,
            @RequestParam(defaultValue="id") String sortBy) {
        return ResponseEntity.ok(ApiResponse.ok("Products fetched", productService.getAllProducts(page, size, sortBy)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<ProductResponse>>> search(
            @RequestParam(required=false) String q,
            @RequestParam(required=false) Long categoryId,
            @RequestParam(required=false) BigDecimal maxPrice,
            @RequestParam(defaultValue="0") int page,
            @RequestParam(defaultValue="12") int size) {
        return ResponseEntity.ok(ApiResponse.ok("Search results", productService.searchProducts(q, categoryId, maxPrice, page, size)));
    }

    @GetMapping("/featured")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getFeatured() {
        return ResponseEntity.ok(ApiResponse.ok("Featured products", productService.getFeatured()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Product details", productService.getById(id)));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(ApiResponse.ok("Products by category", productService.getByCategory(categoryId)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductResponse>> create(@Valid @RequestBody ProductRequest req) {
        return ResponseEntity.ok(ApiResponse.ok("Product created", productService.create(req)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductResponse>> update(@PathVariable Long id, @Valid @RequestBody ProductRequest req) {
        return ResponseEntity.ok(ApiResponse.ok("Product updated", productService.update(id, req)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Product deleted", null));
    }

    @GetMapping("/low-stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getLowStock(
            @RequestParam(defaultValue="10") int threshold) {
        return ResponseEntity.ok(ApiResponse.ok("Low stock products", productService.getLowStock(threshold)));
    }
}
