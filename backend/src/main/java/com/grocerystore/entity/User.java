package com.grocerystore.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity @Table(name="users") @Data @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private String firstName;
    @Column(nullable=false) private String lastName;
    @Column(nullable=false,unique=true) private String email;
    @Column(nullable=false) private String password;
    private String phone;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private Role role = Role.USER;
    private boolean enabled = true;
    @CreationTimestamp private LocalDateTime createdAt;
    @UpdateTimestamp private LocalDateTime updatedAt;
    @OneToMany(mappedBy="user",cascade=CascadeType.ALL) private List<Order> orders;
    @OneToMany(mappedBy="user",cascade=CascadeType.ALL) private List<CartItem> cartItems;
    public enum Role { USER, ADMIN }
}
