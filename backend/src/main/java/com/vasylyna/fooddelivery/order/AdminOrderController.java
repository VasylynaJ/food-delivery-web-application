package com.vasylyna.fooddelivery.order;

import com.vasylyna.fooddelivery.order.dto.OrderResponse;
import com.vasylyna.fooddelivery.order.dto.UpdateOrderStatusRequest;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {
    private final OrderService service;

    public AdminOrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping
    public List<OrderResponse> all() {
        return service.all();
    }

    @PatchMapping("/{id}/status")
    public OrderResponse status(@PathVariable Long id, @Valid @RequestBody UpdateOrderStatusRequest request) {
        return service.status(id, request.status());
    }
}
