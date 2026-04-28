package com.grocerystore.dto;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderResponse {
    private Long id;
    private String orderNumber;
    private List<OrderItemDto> orderItems;
    private BigDecimal subtotal;
    private BigDecimal deliveryCharge;
    private BigDecimal discount;
    private BigDecimal totalAmount;
    private String status;
    private String paymentStatus;
    private String paymentMethod;
    private String addressLine1;
    private String city;
    private String state;
    private String pinCode;
    private String deliverySlot;
    private LocalDateTime createdAt;

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class OrderItemDto {
        private Long productId;
        private String productName;
        private String productEmoji;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal totalPrice;
    }
}
