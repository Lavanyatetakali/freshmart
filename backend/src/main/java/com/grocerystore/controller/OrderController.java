package com.grocerystore.controller;
import com.grocerystore.dto.*;
import com.grocerystore.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> placeOrder(@Valid @RequestBody OrderRequest req) {
        Long userId = userService.getCurrentUser().getId();
        return ResponseEntity.ok(ApiResponse.ok("Order placed successfully", orderService.placeOrder(userId, req)));
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getMyOrders() {
        Long userId = userService.getCurrentUser().getId();
        return ResponseEntity.ok(ApiResponse.ok("Orders fetched", orderService.getUserOrders(userId)));
    }

    @GetMapping("/my/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderDetail(@PathVariable Long orderId) {
        Long userId = userService.getCurrentUser().getId();
        return ResponseEntity.ok(ApiResponse.ok("Order detail", orderService.getOrderById(orderId, userId)));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getAllOrders() {
        return ResponseEntity.ok(ApiResponse.ok("All orders", orderService.getAllOrders()));
    }

    @PatchMapping("/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<OrderResponse>> updateStatus(
            @PathVariable Long orderId, @RequestParam String status) {
        return ResponseEntity.ok(ApiResponse.ok("Order status updated", orderService.updateStatus(orderId, status)));
    }
}
