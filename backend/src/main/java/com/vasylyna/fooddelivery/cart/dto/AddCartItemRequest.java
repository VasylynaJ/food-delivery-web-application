package com.vasylyna.fooddelivery.cart.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AddCartItemRequest(@NotNull Long menuItemId, @Min(1) @Max(50) int quantity) {
}
