package com.vasylyna.fooddelivery.restaurant.dto;

import com.vasylyna.fooddelivery.restaurant.Restaurant;
import java.math.BigDecimal;

public record RestaurantResponse(Long id, String name, String description, String cuisine, BigDecimal rating,
        BigDecimal deliveryFee, String openingHours, String imageUrl, boolean open) {
    public static RestaurantResponse from(Restaurant restaurant) {
        return new RestaurantResponse(restaurant.getId(), restaurant.getName(), restaurant.getDescription(),
                restaurant.getCuisine(), restaurant.getRating(), restaurant.getDeliveryFee(),
                restaurant.getOpeningHours(), restaurant.getImageUrl(), restaurant.isOpen());
    }
}
