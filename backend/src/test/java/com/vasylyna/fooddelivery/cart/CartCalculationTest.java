package com.vasylyna.fooddelivery.cart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import com.vasylyna.fooddelivery.menu.MenuItemRepository;import com.vasylyna.fooddelivery.user.AppUserRepository;
import com.vasylyna.fooddelivery.menu.MenuItem;
import com.vasylyna.fooddelivery.menu.Category;
import com.vasylyna.fooddelivery.restaurant.Restaurant;
import java.math.BigDecimal;import java.util.List;
import org.junit.jupiter.api.Test;

class CartCalculationTest {
 @Test void emptyCartHasNoDeliveryFee(){
  CartRepository carts=mock(CartRepository.class);CartItemRepository items=mock(CartItemRepository.class);MenuItemRepository menu=mock(MenuItemRepository.class);AppUserRepository users=mock(AppUserRepository.class);
  Cart cart=mock(Cart.class);when(carts.findByEmail("a@b.test")).thenReturn(java.util.Optional.of(cart));when(cart.getId()).thenReturn(4L);when(items.findByCart_Id(4L)).thenReturn(List.of());
  var view=new CartService(carts,items,menu,users).get("a@b.test");assertThat(view.subtotal()).isEqualByComparingTo(BigDecimal.ZERO);assertThat(view.deliveryFee()).isEqualByComparingTo(BigDecimal.ZERO);
 }
 @Test void totalIncludesQuantityAndDeliveryFee(){
  CartRepository carts=mock(CartRepository.class);CartItemRepository items=mock(CartItemRepository.class);Cart cart=mock(Cart.class);when(carts.findByEmail("a@b.test")).thenReturn(java.util.Optional.of(cart));when(cart.getId()).thenReturn(5L);
  var food=mock(com.vasylyna.fooddelivery.menu.MenuItem.class);when(food.getName()).thenReturn("Noodles");when(food.getPrice()).thenReturn(new BigDecimal("8.25"));Category category=mock(Category.class);Restaurant restaurant=mock(Restaurant.class);when(food.getCategory()).thenReturn(category);when(category.getRestaurant()).thenReturn(restaurant);when(restaurant.getDeliveryFee()).thenReturn(new BigDecimal("2.50"));var line=mock(CartItem.class);when(line.getMenuItem()).thenReturn(food);when(line.getQuantity()).thenReturn(2);when(items.findByCart_Id(5L)).thenReturn(List.of(line));
  var service=new CartService(carts,items,mock(MenuItemRepository.class),mock(AppUserRepository.class));var view=service.get("a@b.test");assertThat(view.subtotal()).isEqualByComparingTo("16.50");assertThat(view.deliveryFee()).isEqualByComparingTo("2.50");assertThat(view.total()).isEqualByComparingTo("19.00");
 }
}
