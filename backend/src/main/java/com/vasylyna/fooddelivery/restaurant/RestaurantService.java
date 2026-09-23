package com.vasylyna.fooddelivery.restaurant;

import com.vasylyna.fooddelivery.common.ResourceNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RestaurantService {
    private final RestaurantRepository repository;
    public RestaurantService(RestaurantRepository repository) { this.repository = repository; }
    public List<RestaurantDtos.RestaurantResponse> search(String q, String cuisine) {
        return repository.findDistinctByNameContainingIgnoreCaseAndCuisineContainingIgnoreCase(q == null ? "" : q, cuisine == null ? "" : cuisine)
                .stream().map(RestaurantDtos.RestaurantResponse::from).toList();
    }
    public RestaurantDtos.RestaurantResponse get(Long id) {
        return RestaurantDtos.RestaurantResponse.from(repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Restaurant " + id + " was not found")));
    }
}
