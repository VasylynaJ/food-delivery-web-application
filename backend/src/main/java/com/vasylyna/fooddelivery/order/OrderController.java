package com.vasylyna.fooddelivery.order;

import com.vasylyna.fooddelivery.order.dto.CheckoutRequest;
import com.vasylyna.fooddelivery.order.dto.OrderResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse checkout(Authentication authentication, @Valid @RequestBody CheckoutRequest request) {
        return service.checkout(authentication.getName(), request);
    }

    @GetMapping
    public List<OrderResponse> history(Authentication authentication) {
        return service.history(authentication.getName());
    }

    @GetMapping("/{id}")
    public OrderResponse get(Authentication authentication, @PathVariable Long id) {
        return service.get(authentication.getName(), id);
    }
}
