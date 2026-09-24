package com.vasylyna.fooddelivery.menu.dto;

import com.vasylyna.fooddelivery.menu.MenuItem;
import java.math.BigDecimal;

public record MenuItemResponse(Long id, String category, String name, String description, BigDecimal price,
        String imageUrl, boolean available) {
    public static MenuItemResponse from(MenuItem item) {
        return new MenuItemResponse(item.getId(), item.getCategory().getName(), item.getName(), item.getDescription(),
                item.getPrice(), item.getImageUrl(), item.isAvailable());
    }
}
