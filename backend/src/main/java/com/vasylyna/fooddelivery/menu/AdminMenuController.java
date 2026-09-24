package com.vasylyna.fooddelivery.menu;

import com.vasylyna.fooddelivery.menu.dto.MenuItemRequest;
import com.vasylyna.fooddelivery.menu.dto.MenuItemResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/restaurants/{restaurantId}/menu")
public class AdminMenuController {
    private final MenuService service;

    public AdminMenuController(MenuService service) {
        this.service = service;
    }

    @GetMapping
    public List<MenuItemResponse> list(@PathVariable Long restaurantId) {
        return service.listForAdmin(restaurantId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MenuItemResponse create(@PathVariable Long restaurantId, @Valid @RequestBody MenuItemRequest request) {
        return service.createForAdmin(restaurantId, request.category(), request.name(), request.description(),
                request.price(), request.imageUrl(), request.available());
    }

    @PutMapping("/{id}")
    public MenuItemResponse update(@PathVariable Long restaurantId, @PathVariable Long id,
            @Valid @RequestBody MenuItemRequest request) {
        return service.updateForAdmin(restaurantId, id, request.category(), request.name(), request.description(),
                request.price(), request.imageUrl(), request.available());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long restaurantId, @PathVariable Long id) {
        service.deleteForAdmin(restaurantId, id);
    }
}
