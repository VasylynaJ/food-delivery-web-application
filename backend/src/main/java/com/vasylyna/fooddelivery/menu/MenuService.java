package com.vasylyna.fooddelivery.menu;
import java.util.List;import org.springframework.stereotype.Service;import org.springframework.transaction.annotation.Transactional;
@Service public class MenuService{
 private final MenuItemRepository items;public MenuService(MenuItemRepository items){this.items=items;}
 @Transactional(readOnly=true) public List<MenuController.MenuItemResponse> list(Long restaurantId){return items.findByCategory_Restaurant_IdAndAvailableTrueOrderByCategory_NameAscNameAsc(restaurantId).stream().map(MenuController.MenuItemResponse::from).toList();}
}
