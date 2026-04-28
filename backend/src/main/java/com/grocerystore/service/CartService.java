package com.grocerystore.service;
import com.grocerystore.dto.*;
import com.grocerystore.entity.*;
import com.grocerystore.exception.ResourceNotFoundException;
import com.grocerystore.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartSummaryResponse getCart(Long userId) {
        List<CartItem> items = cartItemRepository.findByUserId(userId);
        List<CartItemResponse> dtos = items.stream().map(this::toDto).collect(Collectors.toList());
        BigDecimal subtotal = dtos.stream()
            .map(CartItemResponse::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal delivery = subtotal.compareTo(new BigDecimal("499")) >= 0 ? BigDecimal.ZERO : new BigDecimal("49");
        return CartSummaryResponse.builder()
            .items(dtos).subtotal(subtotal).deliveryCharge(delivery)
            .discount(BigDecimal.ZERO).total(subtotal.add(delivery))
            .itemCount(dtos.stream().mapToInt(CartItemResponse::getQuantity).sum()).build();
    }

    @Transactional
    public CartSummaryResponse addOrUpdate(Long userId, CartItemRequest req) {
        Product product = productRepository.findById(req.getProductId())
            .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        if (product.getStockQuantity() < req.getQuantity())
            throw new IllegalArgumentException("Only " + product.getStockQuantity() + " items in stock");
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        var existing = cartItemRepository.findByUserIdAndProductId(userId, req.getProductId());
        if (existing.isPresent()) {
            existing.get().setQuantity(req.getQuantity());
            cartItemRepository.save(existing.get());
        } else {
            cartItemRepository.save(CartItem.builder()
                .user(user).product(product).quantity(req.getQuantity()).build());
        }
        return getCart(userId);
    }

    @Transactional
    public CartSummaryResponse removeItem(Long userId, Long productId) {
        var item = cartItemRepository.findByUserIdAndProductId(userId, productId)
            .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        cartItemRepository.delete(item);
        return getCart(userId);
    }

    @Transactional
    public void clearCart(Long userId) { cartItemRepository.deleteByUserId(userId); }

    private CartItemResponse toDto(CartItem c) {
        BigDecimal total = c.getProduct().getPrice().multiply(new BigDecimal(c.getQuantity()));
        return CartItemResponse.builder()
            .id(c.getId()).productId(c.getProduct().getId())
            .productName(c.getProduct().getName()).productEmoji(c.getProduct().getEmoji())
            .unit(c.getProduct().getUnit()).unitPrice(c.getProduct().getPrice())
            .quantity(c.getQuantity()).totalPrice(total)
            .stockQuantity(c.getProduct().getStockQuantity()).build();
    }
}
