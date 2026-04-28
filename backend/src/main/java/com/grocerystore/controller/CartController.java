package com.grocerystore.controller;
import com.grocerystore.dto.*;
import com.grocerystore.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CartController {
    private final CartService cartService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<CartSummaryResponse>> getCart() {
        Long userId = userService.getCurrentUser().getId();
        return ResponseEntity.ok(ApiResponse.ok("Cart fetched", cartService.getCart(userId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CartSummaryResponse>> addToCart(@Valid @RequestBody CartItemRequest req) {
        Long userId = userService.getCurrentUser().getId();
        return ResponseEntity.ok(ApiResponse.ok("Cart updated", cartService.addOrUpdate(userId, req)));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<CartSummaryResponse>> updateCart(@Valid @RequestBody CartItemRequest req) {
        Long userId = userService.getCurrentUser().getId();
        return ResponseEntity.ok(ApiResponse.ok("Cart updated", cartService.addOrUpdate(userId, req)));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<CartSummaryResponse>> removeFromCart(@PathVariable Long productId) {
        Long userId = userService.getCurrentUser().getId();
        return ResponseEntity.ok(ApiResponse.ok("Item removed", cartService.removeItem(userId, productId)));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> clearCart() {
        cartService.clearCart(userService.getCurrentUser().getId());
        return ResponseEntity.ok(ApiResponse.ok("Cart cleared", null));
    }
}
