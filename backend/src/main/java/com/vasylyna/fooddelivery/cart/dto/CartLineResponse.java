package com.vasylyna.fooddelivery.cart.dto;

import java.math.BigDecimal;

public record CartLineResponse(Long menuItemId, String name, BigDecimal unitPrice, int quantity, BigDecimal lineTotal) {
}
