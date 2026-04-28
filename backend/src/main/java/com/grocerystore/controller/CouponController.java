package com.grocerystore.controller;
import com.grocerystore.dto.*;
import com.grocerystore.entity.Coupon;
import com.grocerystore.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CouponController {
    private final CouponService couponService;

    @PostMapping("/validate")
    public ResponseEntity<ApiResponse<CouponValidateResponse>> validate(@RequestBody CouponValidateRequest req) {
        return ResponseEntity.ok(ApiResponse.ok("Coupon validated", couponService.validate(req)));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<Coupon>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok("All coupons", couponService.getAllCoupons()));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Coupon>> create(@RequestBody Coupon coupon) {
        return ResponseEntity.ok(ApiResponse.ok("Coupon created", couponService.createCoupon(coupon)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        couponService.deleteCoupon(id);
        return ResponseEntity.ok(ApiResponse.ok("Coupon deleted", null));
    }
}
