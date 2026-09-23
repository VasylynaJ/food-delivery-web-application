package com.vasylyna.fooddelivery.restaurant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import java.util.List;
import org.junit.jupiter.api.Test;

class RestaurantServiceTest {
    @Test void searchPassesQueryAndCuisineToRepository() {
        RestaurantRepository repo=mock(RestaurantRepository.class);
        when(repo.findDistinctByNameContainingIgnoreCaseAndCuisineContainingIgnoreCase("sushi","japanese")).thenReturn(List.of());
        assertThat(new RestaurantService(repo).search("sushi","japanese")).isEmpty();
        verify(repo).findDistinctByNameContainingIgnoreCaseAndCuisineContainingIgnoreCase("sushi","japanese");
    }
}
