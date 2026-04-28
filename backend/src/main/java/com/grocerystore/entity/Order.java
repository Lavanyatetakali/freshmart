package com.grocerystore.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity @Table(name="orders") @Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Order {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(unique=true,nullable=false) private String orderNumber;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="user_id",nullable=false) private User user;
    @OneToMany(mappedBy="order",cascade=CascadeType.ALL) private List<OrderItem> orderItems;
    @Column(nullable=false,precision=10,scale=2) private BigDecimal subtotal;
    @Column(precision=10,scale=2) private BigDecimal deliveryCharge = BigDecimal.ZERO;
    @Column(precision=10,scale=2) private BigDecimal discount = BigDecimal.ZERO;
    @Column(nullable=false,precision=10,scale=2) private BigDecimal totalAmount;
    private String couponCode;
    @Enumerated(EnumType.STRING) private OrderStatus status = OrderStatus.PENDING;
    private String addressLine1; private String addressLine2;
    private String city; private String state; private String pinCode; private String deliveryPhone;
    private String paymentMethod; private String deliverySlot;
    @Enumerated(EnumType.STRING) private PaymentStatus paymentStatus = PaymentStatus.PENDING;
    private String transactionId;
    @CreationTimestamp private LocalDateTime createdAt;
    private LocalDateTime deliveredAt;
    public enum OrderStatus { PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELLED }
    public enum PaymentStatus { PENDING, PAID, FAILED, REFUNDED }
}
