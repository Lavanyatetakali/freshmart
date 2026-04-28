package com.grocerystore.dto;
import lombok.*;
import java.math.BigDecimal;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal oldPrice;
    private String unit;
    private String imageUrl;
    private String emoji;
    private String badge;
    private Integer stockQuantity;
    private Double rating;
    private Integer reviewCount;
    private boolean active;
    private boolean isNew;
    private Long categoryId;
    private String categoryName;
    private Integer discountPercent;
}
