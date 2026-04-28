package com.grocerystore.dto;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CartSummaryResponse {
    private List<CartItemResponse> items;
    private BigDecimal subtotal;
    private BigDecimal deliveryCharge;
    private BigDecimal discount;
    private BigDecimal total;
    private int itemCount;
    private String appliedCoupon;
}
