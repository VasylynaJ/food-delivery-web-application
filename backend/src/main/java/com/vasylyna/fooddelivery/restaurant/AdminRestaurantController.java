package com.vasylyna.fooddelivery.restaurant;

import com.vasylyna.fooddelivery.restaurant.dto.RestaurantRequest;
import com.vasylyna.fooddelivery.restaurant.dto.RestaurantResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/restaurants")
public class AdminRestaurantController {
    private final RestaurantService service;

    public AdminRestaurantController(RestaurantService service) {
        this.service = service;
    }

    @GetMapping
    public List<RestaurantResponse> all() {
        return service.allForAdmin();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestaurantResponse create(@Valid @RequestBody RestaurantRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public RestaurantResponse update(@PathVariable Long id, @Valid @RequestBody RestaurantRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
