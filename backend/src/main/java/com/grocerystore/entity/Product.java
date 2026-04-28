package com.grocerystore.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @Table(name="products") @Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Product {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private String name;
    @Column(columnDefinition="TEXT") private String description;
    @Column(nullable=false,precision=10,scale=2) private BigDecimal price;
    @Column(precision=10,scale=2) private BigDecimal oldPrice;
    private String unit;
    private String imageUrl;
    private String emoji;
    private String badge;
    @Column(nullable=false) private Integer stockQuantity = 0;
    private Double rating = 0.0;
    private Integer reviewCount = 0;
    private boolean active = true;
    private boolean isNew = false;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="category_id",nullable=false) private Category category;
    @CreationTimestamp private LocalDateTime createdAt;
    @UpdateTimestamp private LocalDateTime updatedAt;
}
