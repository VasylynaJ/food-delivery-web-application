package com.vasylyna.fooddelivery.restaurant;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public final class RestaurantDtos {
    private RestaurantDtos() {}
    public record RestaurantResponse(Long id, String name, String description, String cuisine, BigDecimal rating, String openingHours, String imageUrl, boolean open) {
        static RestaurantResponse from(Restaurant r) { return new RestaurantResponse(r.getId(), r.getName(), r.getDescription(), r.getCuisine(), r.getRating(), r.getOpeningHours(), r.getImageUrl(), r.isOpen()); }
    }
    public record RestaurantRequest(@NotBlank @Size(max=160) String name, @Size(max=2000) String description,
            @NotBlank @Size(max=80) String cuisine, @DecimalMin("0.0") @DecimalMax("5.0") BigDecimal rating,
            @NotBlank @Size(max=120) String openingHours, @Size(max=2000) String imageUrl, Boolean open) {}
}
