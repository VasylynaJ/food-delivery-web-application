package com.vasylyna.fooddelivery.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.vasylyna.fooddelivery.common.ResourceNotFoundException;
import com.vasylyna.fooddelivery.restaurant.Restaurant;
import com.vasylyna.fooddelivery.restaurant.RestaurantRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class MenuServiceTest {
    @Test
    void menuListsOnlyAvailableRowsForSelectedRestaurant() {
        MenuItemRepository items = mock(MenuItemRepository.class);
        when(items.findByCategory_Restaurant_IdAndAvailableTrueOrderByCategory_NameAscNameAsc(12L))
                .thenReturn(List.of());

        assertThat(new MenuService(items, mock(RestaurantRepository.class), mock(CategoryRepository.class))
                .list(12L)).isEmpty();

        verify(items).findByCategory_Restaurant_IdAndAvailableTrueOrderByCategory_NameAscNameAsc(12L);
    }

    @Test
    void adminCreateFindsOrCreatesCategoryAndSavesMenuItem() {
        MenuItemRepository items = mock(MenuItemRepository.class);
        RestaurantRepository restaurants = mock(RestaurantRepository.class);
        CategoryRepository categories = mock(CategoryRepository.class);
        Restaurant restaurant = mock(Restaurant.class);
        when(restaurants.existsById(12L)).thenReturn(true);
        when(restaurants.getReferenceById(12L)).thenReturn(restaurant);
        when(categories.findByRestaurant_IdAndNameIgnoreCase(12L, "Sides")).thenReturn(Optional.empty());
        when(categories.save(any(Category.class))).thenAnswer(call -> call.getArgument(0));
        when(items.save(any(MenuItem.class))).thenAnswer(call -> call.getArgument(0));

        var response = new MenuService(items, restaurants, categories).createForAdmin(12L, " Sides ", "Fries",
                "Crispy", new BigDecimal("3.50"), null, null);

        assertThat(response.category()).isEqualTo("Sides");
        assertThat(response.name()).isEqualTo("Fries");
        assertThat(response.price()).isEqualByComparingTo("3.50");
        assertThat(response.available()).isTrue();
        verify(categories).save(any(Category.class));
        verify(items).save(argThat(item -> item.getName().equals("Fries") && item.isAvailable()));
    }

    @Test
    void adminUpdateDoesNotAllowItemFromAnotherRestaurant() {
        MenuItemRepository items = mock(MenuItemRepository.class);
        RestaurantRepository restaurants = mock(RestaurantRepository.class);
        CategoryRepository categories = mock(CategoryRepository.class);
        MenuItem item = mock(MenuItem.class);
        Category category = mock(Category.class);
        when(items.findById(3L)).thenReturn(Optional.of(item));
        when(item.getCategory()).thenReturn(category);
        when(category.getRestaurantId()).thenReturn(99L);

        org.assertj.core.api.Assertions.assertThatThrownBy(() ->
                new MenuService(items, restaurants, categories)
                        .updateForAdmin(12L, 3L, "Sides", "Fries", null, BigDecimal.ONE, null, true))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Menu item was not found");
        verifyNoInteractions(categories, restaurants);
    }
}
