package com.vasylyna.fooddelivery.order;

import static org.assertj.core.api.Assertions.assertThat;import static org.mockito.Mockito.*;
import com.vasylyna.fooddelivery.order.dto.CheckoutRequest;
import com.vasylyna.fooddelivery.cart.*;import com.vasylyna.fooddelivery.menu.MenuItem;import com.vasylyna.fooddelivery.menu.Category;import com.vasylyna.fooddelivery.restaurant.Restaurant;import com.vasylyna.fooddelivery.user.*;import java.math.BigDecimal;import java.util.*;
import org.junit.jupiter.api.Test;

class OrderCreationTest {
 @Test void checkoutPersistsPriceSnapshotsAndCalculatedTotal(){
  CartRepository carts=mock(CartRepository.class);CartItemRepository cartItems=mock(CartItemRepository.class);CustomerOrderRepository orders=mock(CustomerOrderRepository.class);OrderItemRepository orderItems=mock(OrderItemRepository.class);AddressRepository addresses=mock(AddressRepository.class);AppUserRepository users=mock(AppUserRepository.class);
  Cart cart=mock(Cart.class);when(cart.getId()).thenReturn(9L);when(carts.findByEmail("person@example.com")).thenReturn(Optional.of(cart));
  AppUser user=mock(AppUser.class);when(users.findByEmailIgnoreCase("person@example.com")).thenReturn(Optional.of(user));
  MenuItem food=mock(MenuItem.class);when(food.getName()).thenReturn("Roasted vegetables");when(food.getPrice()).thenReturn(new BigDecimal("12.25"));when(food.isAvailable()).thenReturn(true);Category category=mock(Category.class);Restaurant restaurant=mock(Restaurant.class);when(food.getCategory()).thenReturn(category);when(category.getRestaurant()).thenReturn(restaurant);when(restaurant.getDeliveryFee()).thenReturn(new BigDecimal("2.50"));
  CartItem line=mock(CartItem.class);when(line.getMenuItem()).thenReturn(food);when(line.getQuantity()).thenReturn(2);when(cartItems.findByCart_Id(9L)).thenReturn(List.of(line));
  Address address=mock(Address.class);when(addresses.save(any(Address.class))).thenReturn(address);
  when(orders.save(any(CustomerOrder.class))).thenAnswer(call->call.getArgument(0));when(orderItems.findByOrder_Id(nullable(Long.class))).thenReturn(List.of());
  var service=new OrderService(carts,cartItems,orders,orderItems,addresses,users);
  var result=service.checkout("person@example.com",new CheckoutRequest("1 Main St","City","10000","Home",null));
  assertThat(result.subtotal()).isEqualByComparingTo("24.50");
  assertThat(result.deliveryFee()).isEqualByComparingTo("2.50");
  assertThat(result.total()).isEqualByComparingTo("27.00");
  assertThat(CartService.subtotalFor(List.of(line))).isEqualByComparingTo(result.subtotal());
  assertThat(CartService.deliveryFeeFor(List.of(line))).isEqualByComparingTo(result.deliveryFee());
  verify(orderItems).save(argThat(i->i.getItemName().equals("Roasted vegetables")&&i.getQuantity()==2&&i.getUnitPrice().compareTo(new BigDecimal("12.25"))==0));verify(cartItems).deleteByCart_Id(9L);
 }
}
