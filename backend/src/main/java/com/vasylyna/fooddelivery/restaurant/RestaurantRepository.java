package com.vasylyna.fooddelivery.restaurant;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findDistinctByNameContainingIgnoreCaseAndCuisineContainingIgnoreCase(String name, String cuisine);
}
