package com.vasylyna.fooddelivery.menu;

import com.vasylyna.fooddelivery.common.ResourceNotFoundException;
import com.vasylyna.fooddelivery.menu.dto.MenuItemResponse;
import com.vasylyna.fooddelivery.restaurant.RestaurantRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuItemRepository items;
    private final RestaurantRepository restaurants;
    private final CategoryRepository categories;

    public MenuService(MenuItemRepository items, RestaurantRepository restaurants, CategoryRepository categories) {
        this.items = items;
        this.restaurants = restaurants;
        this.categories = categories;
    }

    @Transactional(readOnly = true)
    public List<MenuItemResponse> list(Long restaurantId) {
        return items.findByCategory_Restaurant_IdAndAvailableTrueOrderByCategory_NameAscNameAsc(restaurantId)
                .stream().map(MenuItemResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<MenuItemResponse> listForAdmin(Long restaurantId) {
        requireRestaurant(restaurantId);
        return items.findByCategory_Restaurant_IdOrderByCategory_NameAscNameAsc(restaurantId)
                .stream().map(MenuItemResponse::from).toList();
    }

    @Transactional
    public MenuItemResponse createForAdmin(Long restaurantId, String categoryName, String name, String description,
            BigDecimal price, String imageUrl, Boolean available) {
        Category category = category(restaurantId, categoryName);
        MenuItem item = new MenuItem(category, name, description, price, imageUrl, available == null || available);
        return MenuItemResponse.from(items.save(item));
    }

    @Transactional
    public MenuItemResponse updateForAdmin(Long restaurantId, Long itemId, String categoryName, String name,
            String description, BigDecimal price, String imageUrl, Boolean available) {
        MenuItem item = ownedItem(restaurantId, itemId);
        item.update(category(restaurantId, categoryName), name, description, price, imageUrl,
                available == null || available);
        return MenuItemResponse.from(item);
    }

    @Transactional
    public void deleteForAdmin(Long restaurantId, Long itemId) {
        items.delete(ownedItem(restaurantId, itemId));
    }

    private void requireRestaurant(Long id) {
        if (!restaurants.existsById(id)) {
            throw new ResourceNotFoundException("Restaurant was not found");
        }
    }

    private MenuItem ownedItem(Long restaurantId, Long itemId) {
        MenuItem item = items.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item was not found"));
        if (!item.getCategory().getRestaurantId().equals(restaurantId)) {
            throw new ResourceNotFoundException("Menu item was not found");
        }
        return item;
    }

    private Category category(Long restaurantId, String name) {
        requireRestaurant(restaurantId);
        String normalizedName = name.trim();
        return categories.findByRestaurant_IdAndNameIgnoreCase(restaurantId, normalizedName)
                .orElseGet(() -> categories.save(
                        new Category(restaurants.getReferenceById(restaurantId), normalizedName)));
    }
}
