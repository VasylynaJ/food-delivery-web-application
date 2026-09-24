package com.vasylyna.fooddelivery.order;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.vasylyna.fooddelivery.common.BusinessRuleException;
import static org.mockito.Mockito.*;

import com.vasylyna.fooddelivery.cart.CartItemRepository;
import com.vasylyna.fooddelivery.cart.CartRepository;
import com.vasylyna.fooddelivery.cart.Cart;
import com.vasylyna.fooddelivery.cart.CartItem;
import com.vasylyna.fooddelivery.menu.MenuItem;
import com.vasylyna.fooddelivery.order.dto.CheckoutRequest;
import com.vasylyna.fooddelivery.user.AppUser;
import com.vasylyna.fooddelivery.user.AppUserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class CheckoutFailureTest {
    @Test
    void checkoutRejectsAMissingOrEmptyCart() {
        CartRepository carts = mock(CartRepository.class);
        CartItemRepository cartItems = mock(CartItemRepository.class);
        when(carts.findByEmail("person@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service(carts, cartItems).checkout("person@example.com",
                new CheckoutRequest("Street", "City", "10000", "Home", null)))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("Your cart is empty");
        verifyNoInteractions(cartItems);
    }

    @Test
    void checkoutRejectsItemsThatBecameUnavailable() {
        CartRepository carts = mock(CartRepository.class);
        CartItemRepository cartItems = mock(CartItemRepository.class);
        Cart cart = mock(Cart.class);
        when(carts.findByEmail("person@example.com")).thenReturn(Optional.of(cart));
        when(cart.getId()).thenReturn(12L);
        MenuItem menuItem = mock(MenuItem.class);
        when(menuItem.isAvailable()).thenReturn(false);
        CartItem row = mock(CartItem.class);
        when(row.getMenuItem()).thenReturn(menuItem);
        when(cartItems.findByCart_Id(12L)).thenReturn(List.of(row));

        assertThatThrownBy(() -> service(carts, cartItems).checkout("person@example.com",
                new CheckoutRequest("Street", "City", "10000", "Home", null)))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("One or more items in your cart are no longer available");
    }

    private OrderService service(CartRepository carts, CartItemRepository cartItems) {
        AppUserRepository users = mock(AppUserRepository.class);
        when(users.findByEmailIgnoreCase("person@example.com")).thenReturn(Optional.of(mock(AppUser.class)));
        return new OrderService(carts, cartItems, mock(CustomerOrderRepository.class),
                mock(OrderItemRepository.class), mock(AddressRepository.class), users);
    }
}