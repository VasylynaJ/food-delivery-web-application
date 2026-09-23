package com.vasylyna.fooddelivery.menu;
import java.util.Optional;import org.springframework.data.jpa.repository.JpaRepository;
public interface CategoryRepository extends JpaRepository<Category,Long>{Optional<Category> findByRestaurant_IdAndNameIgnoreCase(Long restaurantId,String name);}
