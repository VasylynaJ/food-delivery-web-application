package com.vasylyna.fooddelivery.order.dto;

import java.math.BigDecimal;

public record OrderItemResponse(String itemName, BigDecimal unitPrice, int quantity) {
}
