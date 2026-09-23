package com.vasylyna.fooddelivery.cart;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/cart") public class CartController{
 private final CartService service;public CartController(CartService service){this.service=service;}
 @GetMapping public CartService.CartView get(Authentication a){return service.get(a.getName());}
 @PostMapping("/items") public CartService.CartView add(Authentication a,@Valid @RequestBody ItemRequest r){return service.add(a.getName(),r.menuItemId(),r.quantity());}
 @PutMapping("/items/{id}") public CartService.CartView set(Authentication a,@PathVariable Long id,@Valid @RequestBody QuantityRequest r){return service.set(a.getName(),id,r.quantity());}
 @DeleteMapping("/items/{id}") public CartService.CartView remove(Authentication a,@PathVariable Long id){return service.remove(a.getName(),id);}
 public record ItemRequest(@NotNull Long menuItemId,@Min(1) @Max(50) int quantity){}public record QuantityRequest(@Min(1) @Max(50) int quantity){}
}
