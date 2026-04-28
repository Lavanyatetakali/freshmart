package com.grocerystore.entity;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity @Table(name="categories") @Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Category {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false,unique=true) private String name;
    private String description;
    private String imageUrl;
    private boolean active = true;
    @OneToMany(mappedBy="category") private List<Product> products;
}
