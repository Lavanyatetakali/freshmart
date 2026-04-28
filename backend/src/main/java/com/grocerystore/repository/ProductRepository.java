package com.grocerystore.repository;
import com.grocerystore.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.util.List;
public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findByCategoryIdAndActiveTrue(Long categoryId);
    Page<Product> findByActiveTrue(Pageable pageable);
    @Query("SELECT p FROM Product p WHERE p.active=true AND (:q IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%',:q,'%')))")
    Page<Product> search(@Param("q") String q, Pageable pageable);
    @Query("SELECT p FROM Product p WHERE p.active=true AND (:catId IS NULL OR p.category.id=:catId) AND p.price<=:maxPrice")
    Page<Product> filter(@Param("catId") Long catId, @Param("maxPrice") BigDecimal maxPrice, Pageable pageable);
    List<Product> findTop8ByActiveTrueOrderByReviewCountDesc();
    List<Product> findByStockQuantityLessThanAndActiveTrue(int threshold);
}
