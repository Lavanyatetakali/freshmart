package com.grocerystore.dto;
import lombok.*;

@Data @AllArgsConstructor @Builder
public class AuthResponse {
    private String token;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
}
