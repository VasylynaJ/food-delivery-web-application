package com.vasylyna.fooddelivery.restaurant;

import com.vasylyna.fooddelivery.restaurant.dto.RestaurantResponse;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {
    private final RestaurantService service;

    public RestaurantController(RestaurantService service) {
        this.service = service;
    }

    @GetMapping
    public List<RestaurantResponse> list(@RequestParam(required = false) String q,
            @RequestParam(required = false) String cuisine) {
        return service.search(q, cuisine);
    }

    @GetMapping("/{id}")
    public RestaurantResponse get(@PathVariable Long id) {
        return service.get(id);
    }
}
