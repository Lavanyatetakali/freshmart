package com.grocerystore.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @Table(name="payments") @Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Payment {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @OneToOne @JoinColumn(name="order_id",unique=true) private Order order;
    @Column(precision=10,scale=2) private BigDecimal amount;
    private String method; private String transactionId; private String gatewayResponse;
    @Enumerated(EnumType.STRING) private PaymentStatus status = PaymentStatus.PENDING;
    @CreationTimestamp private LocalDateTime createdAt;
    public enum PaymentStatus { PENDING, SUCCESS, FAILED, REFUNDED }
}
