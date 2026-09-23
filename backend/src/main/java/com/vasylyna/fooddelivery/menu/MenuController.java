package com.vasylyna.fooddelivery.menu;

import com.vasylyna.fooddelivery.common.ResourceNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/restaurants/{restaurantId}/menu")
public class MenuController {
    private final MenuService service;
    public MenuController(MenuService service){this.service=service;}
    @GetMapping public List<MenuItemResponse> list(@PathVariable Long restaurantId){return service.list(restaurantId);}
    public record MenuItemResponse(Long id, String category, String name, String description, BigDecimal price, String imageUrl, boolean available){
        static MenuItemResponse from(MenuItem i){return new MenuItemResponse(i.getId(),i.getCategory().getName(),i.getName(),i.getDescription(),i.getPrice(),i.getImageUrl(),i.isAvailable());}
    }
}
