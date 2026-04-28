package com.grocerystore.entity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity @Table(name="coupons") @Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Coupon {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(unique=true,nullable=false) private String code;
    @Enumerated(EnumType.STRING) private DiscountType discountType;
    @Column(precision=10,scale=2) private BigDecimal discountValue;
    @Column(precision=10,scale=2) private BigDecimal minOrderAmount = BigDecimal.ZERO;
    private Integer usageLimit; private Integer usedCount = 0;
    private LocalDate expiryDate; private boolean active = true;
    public enum DiscountType { PERCENTAGE, FLAT }
}
