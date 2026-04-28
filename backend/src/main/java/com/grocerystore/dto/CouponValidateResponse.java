package com.grocerystore.dto;
import lombok.*;
import java.math.BigDecimal;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CouponValidateResponse {
    private boolean valid;
    private String message;
    private BigDecimal discountAmount;
    private String couponCode;
}
