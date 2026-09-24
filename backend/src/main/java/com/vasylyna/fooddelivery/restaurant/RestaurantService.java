package com.vasylyna.fooddelivery.restaurant;

import com.vasylyna.fooddelivery.common.ResourceNotFoundException;
import com.vasylyna.fooddelivery.restaurant.dto.RestaurantRequest;
import com.vasylyna.fooddelivery.restaurant.dto.RestaurantResponse;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RestaurantService {
    private final RestaurantRepository repository;

    public RestaurantService(RestaurantRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<RestaurantResponse> search(String q, String cuisine) {
        return repository.findDistinctByNameContainingIgnoreCaseAndCuisineContainingIgnoreCase(
                        q == null ? "" : q, cuisine == null ? "" : cuisine)
                .stream().map(RestaurantResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public RestaurantResponse get(Long id) {
        return RestaurantResponse.from(repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant " + id + " was not found")));
    }

    @Transactional(readOnly = true)
    public List<RestaurantResponse> allForAdmin() {
        return repository.findAll().stream().map(RestaurantResponse::from).toList();
    }

    @Transactional
    public RestaurantResponse create(RestaurantRequest request) {
        Restaurant restaurant = new Restaurant(request.name(), request.description(), request.cuisine(),
                rating(request.rating()), request.deliveryFee(), request.openingHours(), request.imageUrl(),
                request.open() == null || request.open());
        return RestaurantResponse.from(repository.save(restaurant));
    }

    @Transactional
    public RestaurantResponse update(Long id, RestaurantRequest request) {
        Restaurant restaurant = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant was not found"));
        restaurant.update(request.name(), request.description(), request.cuisine(), rating(request.rating()),
                request.deliveryFee(), request.openingHours(), request.imageUrl(),
                request.open() == null || request.open());
        return RestaurantResponse.from(repository.save(restaurant));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Restaurant was not found");
        }
        repository.deleteById(id);
    }

    private static BigDecimal rating(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
