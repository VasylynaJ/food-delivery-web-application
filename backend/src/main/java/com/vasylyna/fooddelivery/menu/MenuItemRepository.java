package com.vasylyna.fooddelivery.menu;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    @EntityGraph(attributePaths = "category")
    List<MenuItem> findByCategory_Restaurant_IdAndAvailableTrueOrderByCategory_NameAscNameAsc(Long restaurantId);

    @EntityGraph(attributePaths = "category")
    List<MenuItem> findByCategory_Restaurant_IdOrderByCategory_NameAscNameAsc(Long restaurantId);
}
