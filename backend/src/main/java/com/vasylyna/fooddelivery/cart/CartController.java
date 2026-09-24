package com.vasylyna.fooddelivery.cart;

import com.vasylyna.fooddelivery.cart.dto.AddCartItemRequest;
import com.vasylyna.fooddelivery.cart.dto.CartResponse;
import com.vasylyna.fooddelivery.cart.dto.UpdateCartItemRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService service;

    public CartController(CartService service) {
        this.service = service;
    }

    @GetMapping
    public CartResponse get(Authentication authentication) {
        return service.get(authentication.getName());
    }

    @PostMapping("/items")
    public CartResponse add(Authentication authentication, @Valid @RequestBody AddCartItemRequest request) {
        return service.add(authentication.getName(), request.menuItemId(), request.quantity());
    }

    @PutMapping("/items/{id}")
    public CartResponse set(Authentication authentication, @PathVariable Long id,
            @Valid @RequestBody UpdateCartItemRequest request) {
        return service.set(authentication.getName(), id, request.quantity());
    }

    @DeleteMapping("/items/{id}")
    public CartResponse remove(Authentication authentication, @PathVariable Long id) {
        return service.remove(authentication.getName(), id);
    }
}
