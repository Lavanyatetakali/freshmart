package com.grocerystore.dto;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductRequest {
    @NotBlank private String name;
    private String description;
    @NotNull @DecimalMin("0.01") private BigDecimal price;
    private BigDecimal oldPrice;
    @NotBlank private String unit;
    private String imageUrl;
    private String emoji;
    private String badge;
    @NotNull @Min(0) private Integer stockQuantity;
    @NotNull private Long categoryId;
    private boolean isNew;
}
