package com.vasylyna.fooddelivery.order.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponse(Long id, String status, BigDecimal subtotal, BigDecimal deliveryFee, BigDecimal total,
        Instant createdAt, String street, String city, String postalCode, List<OrderItemResponse> items) {
}
