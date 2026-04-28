package com.grocerystore.service;
import com.grocerystore.dto.*;
import com.grocerystore.entity.Coupon;
import com.grocerystore.exception.ResourceNotFoundException;
import com.grocerystore.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;

    public CouponValidateResponse validate(CouponValidateRequest req) {
        var opt = couponRepository.findByCodeAndActiveTrue(req.getCode().toUpperCase());
        if (opt.isEmpty())
            return CouponValidateResponse.builder().valid(false).message("Invalid or expired coupon code.").build();
        Coupon c = opt.get();
        if (c.getExpiryDate() != null && c.getExpiryDate().isBefore(LocalDate.now()))
            return CouponValidateResponse.builder().valid(false).message("Coupon has expired.").build();
        if (req.getOrderAmount().compareTo(c.getMinOrderAmount()) < 0)
            return CouponValidateResponse.builder().valid(false)
                .message("Minimum order ₹" + c.getMinOrderAmount() + " required.").build();
        BigDecimal disc = c.getDiscountType() == Coupon.DiscountType.PERCENTAGE
            ? req.getOrderAmount().multiply(c.getDiscountValue()).divide(new BigDecimal(100), 2, java.math.RoundingMode.HALF_UP)
            : c.getDiscountValue();
        return CouponValidateResponse.builder().valid(true)
            .message("Coupon applied! You save ₹" + disc)
            .discountAmount(disc).couponCode(c.getCode()).build();
    }

    public List<Coupon> getAllCoupons() { return couponRepository.findAll(); }

    public Coupon createCoupon(Coupon coupon) { return couponRepository.save(coupon); }

    public void deleteCoupon(Long id) {
        couponRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Coupon not found"));
        couponRepository.deleteById(id);
    }
}
