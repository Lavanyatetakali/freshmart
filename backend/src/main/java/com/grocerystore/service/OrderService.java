package com.grocerystore.service;
import com.grocerystore.dto.*;
import com.grocerystore.entity.*;
import com.grocerystore.exception.ResourceNotFoundException;
import com.grocerystore.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;

    @Transactional
    public OrderResponse placeOrder(Long userId, OrderRequest req) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        if (cartItems.isEmpty()) throw new IllegalStateException("Cart is empty");

        BigDecimal subtotal = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem ci : cartItems) {
            Product p = ci.getProduct();
            if (p.getStockQuantity() < ci.getQuantity())
                throw new IllegalArgumentException("Insufficient stock for: " + p.getName());
            BigDecimal lineTotal = p.getPrice().multiply(new BigDecimal(ci.getQuantity()));
            subtotal = subtotal.add(lineTotal);
            orderItems.add(OrderItem.builder()
                .product(p).quantity(ci.getQuantity())
                .unitPrice(p.getPrice()).totalPrice(lineTotal).build());
            p.setStockQuantity(p.getStockQuantity() - ci.getQuantity());
            productRepository.save(p);
        }

        BigDecimal delivery = subtotal.compareTo(new BigDecimal("499")) >= 0 ? BigDecimal.ZERO : new BigDecimal("49");
        BigDecimal discount = BigDecimal.ZERO;

        if (req.getCouponCode() != null && !req.getCouponCode().isBlank()) {
            var couponOpt = couponRepository.findByCodeAndActiveTrue(req.getCouponCode().toUpperCase());
            if (couponOpt.isPresent()) {
                Coupon c = couponOpt.get();
                if (subtotal.compareTo(c.getMinOrderAmount()) >= 0) {
                    discount = c.getDiscountType() == Coupon.DiscountType.PERCENTAGE
                        ? subtotal.multiply(c.getDiscountValue()).divide(new BigDecimal(100), 2, java.math.RoundingMode.HALF_UP)
                        : c.getDiscountValue();
                    c.setUsedCount(c.getUsedCount() + 1);
                    couponRepository.save(c);
                }
            }
        }

        String orderNum = "FM-" + System.currentTimeMillis();
        BigDecimal total = subtotal.add(delivery).subtract(discount);

        Order order = Order.builder()
            .orderNumber(orderNum).user(user)
            .subtotal(subtotal).deliveryCharge(delivery)
            .discount(discount).totalAmount(total)
            .couponCode(req.getCouponCode())
            .status(Order.OrderStatus.CONFIRMED)
            .addressLine1(req.getAddressLine1()).addressLine2(req.getAddressLine2())
            .city(req.getCity()).state(req.getState()).pinCode(req.getPinCode())
            .deliveryPhone(req.getDeliveryPhone())
            .paymentMethod(req.getPaymentMethod()).deliverySlot(req.getDeliverySlot())
            .paymentStatus("COD".equalsIgnoreCase(req.getPaymentMethod())
                ? Order.PaymentStatus.PENDING : Order.PaymentStatus.PAID)
            .build();

        Order saved = orderRepository.save(order);
        orderItems.forEach(i -> { i.setOrder(saved); });
        saved.setOrderItems(orderItems);
        orderRepository.save(saved);
        cartItemRepository.deleteByUserId(userId);
        return toDto(saved);
    }

    public List<OrderResponse> getUserOrders(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId)
            .stream().map(this::toDto).collect(Collectors.toList());
    }

    public OrderResponse getOrderById(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));
        if (!order.getUser().getId().equals(userId))
            throw new SecurityException("Not authorized to view this order");
        return toDto(order);
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse updateStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        order.setStatus(Order.OrderStatus.valueOf(status.toUpperCase()));
        return toDto(orderRepository.save(order));
    }

    private OrderResponse toDto(Order o) {
        List<OrderResponse.OrderItemDto> items = o.getOrderItems() == null ? List.of() :
            o.getOrderItems().stream().map(i -> OrderResponse.OrderItemDto.builder()
                .productId(i.getProduct().getId()).productName(i.getProduct().getName())
                .productEmoji(i.getProduct().getEmoji()).quantity(i.getQuantity())
                .unitPrice(i.getUnitPrice()).totalPrice(i.getTotalPrice()).build()
            ).collect(Collectors.toList());
        return OrderResponse.builder()
            .id(o.getId()).orderNumber(o.getOrderNumber()).orderItems(items)
            .subtotal(o.getSubtotal()).deliveryCharge(o.getDeliveryCharge())
            .discount(o.getDiscount()).totalAmount(o.getTotalAmount())
            .status(o.getStatus().name()).paymentStatus(o.getPaymentStatus().name())
            .paymentMethod(o.getPaymentMethod()).addressLine1(o.getAddressLine1())
            .city(o.getCity()).state(o.getState()).pinCode(o.getPinCode())
            .deliverySlot(o.getDeliverySlot()).createdAt(o.getCreatedAt()).build();
    }
}
