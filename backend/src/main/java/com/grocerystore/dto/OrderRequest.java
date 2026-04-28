package com.grocerystore.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderRequest {
    @NotBlank private String addressLine1;
    private String addressLine2;
    @NotBlank private String city;
    @NotBlank private String state;
    @NotBlank private String pinCode;
    @NotBlank private String deliveryPhone;
    @NotBlank private String paymentMethod;
    private String deliverySlot;
    private String couponCode;
}
