package com.vasylyna.fooddelivery.cart.dto;

import java.math.BigDecimal;
import java.util.List;

public record CartResponse(List<CartLineResponse> items, BigDecimal subtotal, BigDecimal deliveryFee, BigDecimal total) {
}
