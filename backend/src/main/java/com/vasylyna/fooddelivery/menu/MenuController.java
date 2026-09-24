package com.vasylyna.fooddelivery.menu;

import com.vasylyna.fooddelivery.menu.dto.MenuItemResponse;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/restaurants/{restaurantId}/menu")
public class MenuController {
    private final MenuService service;

    public MenuController(MenuService service) {
        this.service = service;
    }

    @GetMapping
    public List<MenuItemResponse> list(@PathVariable Long restaurantId) {
        return service.list(restaurantId);
    }
}
