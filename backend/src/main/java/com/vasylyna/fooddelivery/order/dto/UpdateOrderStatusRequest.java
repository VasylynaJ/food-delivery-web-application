package com.vasylyna.fooddelivery.order.dto;

import com.vasylyna.fooddelivery.order.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderStatusRequest(@NotNull OrderStatus status) {
}
