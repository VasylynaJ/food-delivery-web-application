package com.vasylyna.fooddelivery.cart;
import com.vasylyna.fooddelivery.common.ResourceNotFoundException;
import com.vasylyna.fooddelivery.menu.*;
import com.vasylyna.fooddelivery.user.*;
import java.math.*;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service public class CartService {
 private final CartRepository carts;private final CartItemRepository items;private final MenuItemRepository menu;private final AppUserRepository users;
 public CartService(CartRepository carts,CartItemRepository items,MenuItemRepository menu,AppUserRepository users){this.carts=carts;this.items=items;this.menu=menu;this.users=users;}
 @Transactional public CartView get(String email){return view(cart(email));}
 @Transactional public CartView add(String email,Long itemId,int qty){if(qty<1||qty>50)throw new IllegalArgumentException("Quantity must be between 1 and 50");Cart c=cart(email);MenuItem m=menu.findById(itemId).orElseThrow(()->new ResourceNotFoundException("Menu item was not found"));if(!m.isAvailable())throw new IllegalArgumentException("Menu item is unavailable");var existing=items.findByCart_IdAndMenuItem_Id(c.getId(),itemId);var current=items.findByCart_Id(c.getId());if(!current.isEmpty()&&!current.get(0).getMenuItem().getCategory().getRestaurantId().equals(m.getCategory().getRestaurantId()))throw new IllegalArgumentException("A cart can contain items from one restaurant at a time");if(existing.isPresent()){int newQuantity=existing.get().getQuantity()+qty;if(newQuantity>50)throw new IllegalArgumentException("Cart item quantity cannot exceed 50");existing.get().setQuantity(newQuantity);}else items.save(new CartItem(c,m,qty));c.touch();return view(c);}
 @Transactional public CartView set(String email,Long itemId,int qty){Cart c=cart(email);var row=items.findByCart_IdAndMenuItem_Id(c.getId(),itemId).orElseThrow(()->new ResourceNotFoundException("Cart item was not found"));if(qty<1||qty>50)throw new IllegalArgumentException("Quantity must be between 1 and 50");row.setQuantity(qty);c.touch();return view(c);}
 @Transactional public CartView remove(String email,Long itemId){Cart c=cart(email);items.deleteByCart_IdAndMenuItem_Id(c.getId(),itemId);c.touch();return view(c);}
 @Transactional public void clear(String email){Cart c=cart(email);items.deleteByCart_Id(c.getId());c.touch();}
 private Cart cart(String email){return carts.findByEmail(email).orElseGet(()->carts.save(new Cart(users.findByEmailIgnoreCase(email).orElseThrow())));}
 public static BigDecimal deliveryFeeFor(List<CartItem> rows){return rows.isEmpty()?BigDecimal.ZERO.setScale(2):rows.get(0).getMenuItem().getCategory().getRestaurant().getDeliveryFee();}
 private CartView view(Cart c){var rows=items.findByCart_Id(c.getId());var lines=rows.stream().map(x->new CartLine(x.getMenuItem().getId(),x.getMenuItem().getName(),x.getMenuItem().getPrice(),x.getQuantity(),x.getMenuItem().getPrice().multiply(BigDecimal.valueOf(x.getQuantity())))).toList();BigDecimal subtotal=lines.stream().map(CartLine::lineTotal).reduce(BigDecimal.ZERO,BigDecimal::add).setScale(2,RoundingMode.HALF_UP);BigDecimal fee=deliveryFeeFor(rows);return new CartView(lines,subtotal,fee,subtotal.add(fee));}
 public record CartLine(Long menuItemId,String name,BigDecimal unitPrice,int quantity,BigDecimal lineTotal){} public record CartView(List<CartLine> items,BigDecimal subtotal,BigDecimal deliveryFee,BigDecimal total){}
}
