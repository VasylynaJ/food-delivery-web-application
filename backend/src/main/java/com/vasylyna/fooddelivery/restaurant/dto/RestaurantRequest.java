package com.vasylyna.fooddelivery.restaurant.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record RestaurantRequest(
        @NotBlank @Size(max = 160) String name,
        @Size(max = 2000) String description,
        @NotBlank @Size(max = 80) String cuisine,
        @NotNull @DecimalMin("0.0") @DecimalMax("5.0") @Digits(integer = 1, fraction = 1) BigDecimal rating,
        @NotNull @DecimalMin("0.0") @Digits(integer = 8, fraction = 2) BigDecimal deliveryFee,
        @NotBlank @Size(max = 120) String openingHours,
        @Size(max = 2000) String imageUrl,
        Boolean open) {
}
