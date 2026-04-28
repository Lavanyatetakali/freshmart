package com.grocerystore.service;
import com.grocerystore.dto.*;
import com.grocerystore.entity.Product;
import com.grocerystore.exception.ResourceNotFoundException;
import com.grocerystore.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public PageResponse<ProductResponse> getAllProducts(int page, int size, String sortBy) {
        Sort sort = switch (sortBy) {
            case "price_asc"  -> Sort.by("price").ascending();
            case "price_desc" -> Sort.by("price").descending();
            case "rating"     -> Sort.by("rating").descending();
            default           -> Sort.by("id").ascending();
        };
        Page<Product> p = productRepository.findByActiveTrue(PageRequest.of(page, size, sort));
        return toPage(p);
    }

    public PageResponse<ProductResponse> searchProducts(String q, Long catId, BigDecimal maxPrice, int page, int size) {
        if (q != null && !q.isBlank()) {
            Page<Product> p = productRepository.search(q, PageRequest.of(page, size));
            return toPage(p);
        }
        Page<Product> p = productRepository.filter(catId, maxPrice != null ? maxPrice : new BigDecimal("9999"), PageRequest.of(page, size));
        return toPage(p);
    }

    public ProductResponse getById(Long id) {
        Product p = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
        return toDto(p);
    }

    public List<ProductResponse> getByCategory(Long catId) {
        return productRepository.findByCategoryIdAndActiveTrue(catId).stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<ProductResponse> getFeatured() {
        return productRepository.findTop8ByActiveTrueOrderByReviewCountDesc().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
    public ProductResponse create(ProductRequest req) {
        var cat = categoryRepository.findById(req.getCategoryId())
            .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + req.getCategoryId()));
        var p = Product.builder()
            .name(req.getName()).description(req.getDescription())
            .price(req.getPrice()).oldPrice(req.getOldPrice())
            .unit(req.getUnit()).imageUrl(req.getImageUrl())
            .emoji(req.getEmoji()).badge(req.getBadge())
            .stockQuantity(req.getStockQuantity())
            .category(cat).isNew(req.isNew()).active(true).build();
        return toDto(productRepository.save(p));
    }

    @Transactional
    public ProductResponse update(Long id, ProductRequest req) {
        var p = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
        var cat = categoryRepository.findById(req.getCategoryId())
            .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + req.getCategoryId()));
        p.setName(req.getName()); p.setDescription(req.getDescription());
        p.setPrice(req.getPrice()); p.setOldPrice(req.getOldPrice());
        p.setUnit(req.getUnit()); p.setImageUrl(req.getImageUrl());
        p.setEmoji(req.getEmoji()); p.setBadge(req.getBadge());
        p.setStockQuantity(req.getStockQuantity()); p.setCategory(cat);
        return toDto(productRepository.save(p));
    }

    @Transactional
    public void delete(Long id) {
        var p = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
        p.setActive(false);
        productRepository.save(p);
    }

    public List<ProductResponse> getLowStock(int threshold) {
        return productRepository.findByStockQuantityLessThanAndActiveTrue(threshold)
            .stream().map(this::toDto).collect(Collectors.toList());
    }

    private PageResponse<ProductResponse> toPage(Page<Product> p) {
        return PageResponse.<ProductResponse>builder()
            .content(p.getContent().stream().map(this::toDto).collect(Collectors.toList()))
            .page(p.getNumber()).size(p.getSize())
            .totalElements(p.getTotalElements()).totalPages(p.getTotalPages())
            .last(p.isLast()).build();
    }

    public ProductResponse toDto(Product p) {
        int disc = 0;
        if (p.getOldPrice() != null && p.getOldPrice().compareTo(BigDecimal.ZERO) > 0)
            disc = p.getOldPrice().subtract(p.getPrice()).multiply(new BigDecimal(100))
                    .divide(p.getOldPrice(), 0, java.math.RoundingMode.HALF_UP).intValue();
        return ProductResponse.builder()
            .id(p.getId()).name(p.getName()).description(p.getDescription())
            .price(p.getPrice()).oldPrice(p.getOldPrice()).unit(p.getUnit())
            .imageUrl(p.getImageUrl()).emoji(p.getEmoji()).badge(p.getBadge())
            .stockQuantity(p.getStockQuantity()).rating(p.getRating())
            .reviewCount(p.getReviewCount()).active(p.isActive()).isNew(p.isNew())
            .categoryId(p.getCategory().getId()).categoryName(p.getCategory().getName())
            .discountPercent(disc).build();
    }
}
