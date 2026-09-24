package com.vasylyna.fooddelivery.cart;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import com.vasylyna.fooddelivery.common.BusinessRuleException;
import com.vasylyna.fooddelivery.menu.Category;
import com.vasylyna.fooddelivery.menu.MenuItem;
import com.vasylyna.fooddelivery.menu.MenuItemRepository;
import com.vasylyna.fooddelivery.restaurant.Restaurant;
import com.vasylyna.fooddelivery.user.AppUserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class CartRuleTest {
    @Test
    void addRejectsQuantitiesOutsideTheExistingLimit() {
        CartRepository carts = mock(CartRepository.class);
        CartItemRepository items = mock(CartItemRepository.class);
        MenuItemRepository menu = mock(MenuItemRepository.class);

        assertThatThrownBy(() -> service(carts, items, menu).add("person@example.com", 1L, 0))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("Quantity must be between 1 and 50");
        verifyNoInteractions(carts, items, menu);
    }

    @Test
    void addRejectsMixingItemsFromDifferentRestaurants() {
        CartRepository carts = mock(CartRepository.class);
        CartItemRepository items = mock(CartItemRepository.class);
        MenuItemRepository menu = mock(MenuItemRepository.class);
        Cart cart = mock(Cart.class);
        when(carts.findByEmail("person@example.com")).thenReturn(Optional.of(cart));
        when(cart.getId()).thenReturn(9L);

        MenuItem requestedItem = menuItemFromRestaurant(12L);
        when(menu.findById(2L)).thenReturn(Optional.of(requestedItem));
        when(items.findByCart_IdAndMenuItem_Id(9L, 2L)).thenReturn(Optional.empty());
        CartItem existingItem = mock(CartItem.class);
        MenuItem existingFood = menuItemFromRestaurant(7L);
        when(existingItem.getMenuItem()).thenReturn(existingFood);
        when(items.findByCart_Id(9L)).thenReturn(List.of(existingItem));

        assertThatThrownBy(() -> service(carts, items, menu).add("person@example.com", 2L, 1))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("A cart can contain items from one restaurant at a time");
        verify(items, never()).save(any(CartItem.class));
    }

    private MenuItem menuItemFromRestaurant(Long restaurantId) {
        Restaurant restaurant = mock(Restaurant.class);
        when(restaurant.getId()).thenReturn(restaurantId);
        Category category = mock(Category.class);
        when(category.getRestaurantId()).thenReturn(restaurantId);
        when(category.getRestaurant()).thenReturn(restaurant);
        MenuItem item = mock(MenuItem.class);
        when(item.getCategory()).thenReturn(category);
        when(item.isAvailable()).thenReturn(true);
        return item;
    }

    private CartService service(CartRepository carts, CartItemRepository items, MenuItemRepository menu) {
        return new CartService(carts, items, menu, mock(AppUserRepository.class));
    }
}