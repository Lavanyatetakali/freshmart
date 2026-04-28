package com.grocerystore.dto;
import lombok.*;
import java.math.BigDecimal;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CartItemResponse {
    private Long id;
    private Long productId;
    private String productName;
    private String productEmoji;
    private String unit;
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal totalPrice;
    private Integer stockQuantity;
}
