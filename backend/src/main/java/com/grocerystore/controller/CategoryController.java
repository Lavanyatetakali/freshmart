package com.grocerystore.controller;
import com.grocerystore.entity.Category;
import com.grocerystore.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.grocerystore.dto.ApiResponse;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Category>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok("Categories", categoryService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Category>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Category", categoryService.getById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Category>> create(@RequestBody Category cat) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.ok("Category created", categoryService.create(cat)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Category>> update(@PathVariable Long id, @RequestBody Category cat) {
        return ResponseEntity.ok(ApiResponse.ok("Category updated", categoryService.update(id, cat)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Category deleted", null));
    }
}
