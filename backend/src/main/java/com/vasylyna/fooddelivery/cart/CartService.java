package com.vasylyna.fooddelivery.cart;

import com.vasylyna.fooddelivery.cart.dto.CartLineResponse;
import com.vasylyna.fooddelivery.cart.dto.CartResponse;
import com.vasylyna.fooddelivery.common.BusinessRuleException;
import com.vasylyna.fooddelivery.common.ResourceNotFoundException;
import com.vasylyna.fooddelivery.menu.MenuItem;
import com.vasylyna.fooddelivery.menu.MenuItemRepository;
import com.vasylyna.fooddelivery.user.AppUserRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartService {
    private final CartRepository carts;
    private final CartItemRepository items;
    private final MenuItemRepository menu;
    private final AppUserRepository users;

    public CartService(CartRepository carts, CartItemRepository items, MenuItemRepository menu,
            AppUserRepository users) {
        this.carts = carts;
        this.items = items;
        this.menu = menu;
        this.users = users;
    }

    @Transactional
    public CartResponse get(String email) {
        return view(cart(email));
    }

    @Transactional
    public CartResponse add(String email, Long itemId, int qty) {
        if (qty < 1 || qty > 50) {
            throw new BusinessRuleException("Quantity must be between 1 and 50");
        }
        Cart cart = cart(email);
        MenuItem menuItem = menu.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item was not found"));
        if (!menuItem.isAvailable()) {
            throw new BusinessRuleException("Menu item is unavailable");
        }
        var existing = items.findByCart_IdAndMenuItem_Id(cart.getId(), itemId);
        var current = items.findByCart_Id(cart.getId());
        if (!current.isEmpty() && !current.get(0).getMenuItem().getCategory().getRestaurantId()
                .equals(menuItem.getCategory().getRestaurantId())) {
            throw new BusinessRuleException("A cart can contain items from one restaurant at a time");
        }
        if (existing.isPresent()) {
            int newQuantity = existing.get().getQuantity() + qty;
            if (newQuantity > 50) {
                throw new BusinessRuleException("Cart item quantity cannot exceed 50");
            }
            existing.get().setQuantity(newQuantity);
        } else {
            items.save(new CartItem(cart, menuItem, qty));
        }
        cart.touch();
        return view(cart);
    }

    @Transactional
    public CartResponse set(String email, Long itemId, int qty) {
        Cart cart = cart(email);
        var row = items.findByCart_IdAndMenuItem_Id(cart.getId(), itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item was not found"));
        if (qty < 1 || qty > 50) {
            throw new BusinessRuleException("Quantity must be between 1 and 50");
        }
        row.setQuantity(qty);
        cart.touch();
        return view(cart);
    }

    @Transactional
    public CartResponse remove(String email, Long itemId) {
        Cart cart = cart(email);
        items.deleteByCart_IdAndMenuItem_Id(cart.getId(), itemId);
        cart.touch();
        return view(cart);
    }

    @Transactional
    public void clear(String email) {
        Cart cart = cart(email);
        items.deleteByCart_Id(cart.getId());
        cart.touch();
    }

    private Cart cart(String email) {
        return carts.findByEmail(email)
                .orElseGet(() -> carts.save(new Cart(users.findByEmailIgnoreCase(email).orElseThrow())));
    }

    public static BigDecimal deliveryFeeFor(List<CartItem> rows) {
        return rows.isEmpty() ? BigDecimal.ZERO.setScale(2)
                : rows.get(0).getMenuItem().getCategory().getRestaurant().getDeliveryFee();
    }

    public static BigDecimal lineTotalFor(CartItem item) {
        return item.getMenuItem().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
    }

    public static BigDecimal subtotalFor(List<CartItem> rows) {
        return rows.stream()
                .map(CartService::lineTotalFor)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private CartResponse view(Cart cart) {
        var rows = items.findByCart_Id(cart.getId());
        var lines = rows.stream()
                .map(item -> new CartLineResponse(item.getMenuItem().getId(), item.getMenuItem().getName(),
                        item.getMenuItem().getPrice(), item.getQuantity(),
                        lineTotalFor(item)))
                .toList();
        BigDecimal subtotal = subtotalFor(rows);
        BigDecimal fee = deliveryFeeFor(rows);
        return new CartResponse(lines, subtotal, fee, subtotal.add(fee));
    }
}
