package com.vasylyna.fooddelivery.restaurant;

import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {
    private final RestaurantService service;
    public RestaurantController(RestaurantService service) { this.service = service; }
    @GetMapping public List<RestaurantDtos.RestaurantResponse> list(@RequestParam(required=false) String q, @RequestParam(required=false) String cuisine) { return service.search(q, cuisine); }
    @GetMapping("/{id}") public RestaurantDtos.RestaurantResponse get(@PathVariable Long id) { return service.get(id); }
}
