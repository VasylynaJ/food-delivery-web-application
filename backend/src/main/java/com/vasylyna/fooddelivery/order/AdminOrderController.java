package com.vasylyna.fooddelivery.order;
import jakarta.validation.Valid;import jakarta.validation.constraints.NotNull;import org.springframework.web.bind.annotation.*;import java.util.List;
@RestController @RequestMapping("/api/admin/orders") public class AdminOrderController{
 private final OrderService service;public AdminOrderController(OrderService service){this.service=service;}
 @GetMapping public List<OrderService.OrderView> all(){return service.all();}
 @PatchMapping("/{id}/status") public OrderService.OrderView status(@PathVariable Long id,@Valid @RequestBody StatusRequest r){return service.status(id,r.status());}
 public record StatusRequest(@NotNull OrderStatus status){}
}
