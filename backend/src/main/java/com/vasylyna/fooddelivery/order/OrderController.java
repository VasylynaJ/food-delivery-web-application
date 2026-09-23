package com.vasylyna.fooddelivery.order;
import jakarta.validation.Valid;import org.springframework.http.HttpStatus;import org.springframework.security.core.Authentication;import org.springframework.web.bind.annotation.*;import java.util.List;
@RestController @RequestMapping("/api/orders") public class OrderController{
 private final OrderService service;public OrderController(OrderService service){this.service=service;}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public OrderService.OrderView checkout(Authentication a,@Valid @RequestBody OrderService.CheckoutRequest r){return service.checkout(a.getName(),r);}
 @GetMapping public List<OrderService.OrderView> history(Authentication a){return service.history(a.getName());}
 @GetMapping("/{id}") public OrderService.OrderView get(Authentication a,@PathVariable Long id){return service.get(a.getName(),id);}
}
