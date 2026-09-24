package com.vasylyna.fooddelivery.restaurant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import com.vasylyna.fooddelivery.restaurant.dto.RestaurantRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class RestaurantServiceTest {
    @Test
    void searchPassesQueryAndCuisineToRepository() {
        RestaurantRepository repo = mock(RestaurantRepository.class);
        when(repo.findDistinctByNameContainingIgnoreCaseAndCuisineContainingIgnoreCase("sushi", "japanese"))
                .thenReturn(List.of());

        assertThat(new RestaurantService(repo).search("sushi", "japanese")).isEmpty();

        verify(repo).findDistinctByNameContainingIgnoreCaseAndCuisineContainingIgnoreCase("sushi", "japanese");
    }

    @Test
    void createAppliesExistingDefaultsAndReturnsRestaurantResponse() {
        RestaurantRepository repo = mock(RestaurantRepository.class);
        when(repo.save(any(Restaurant.class))).thenAnswer(call -> call.getArgument(0));
        RestaurantService service = new RestaurantService(repo);
        var request = new RestaurantRequest("Cafe", null, "Italian", null,
                new BigDecimal("1.50"), "09:00-17:00", null, null);

        var response = service.create(request);

        assertThat(response.name()).isEqualTo("Cafe");
        assertThat(response.rating()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(response.deliveryFee()).isEqualByComparingTo("1.50");
        assertThat(response.open()).isTrue();
    }

    @Test
    void updateKeepsTheExistingNotFoundBehavior() {
        RestaurantRepository repo = mock(RestaurantRepository.class);
        when(repo.findById(7L)).thenReturn(Optional.empty());

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> new RestaurantService(repo).update(7L,
                new RestaurantRequest("Cafe", null, "Italian", BigDecimal.ONE, BigDecimal.ZERO,
                        "09:00-17:00", null, true)))
                .isInstanceOf(com.vasylyna.fooddelivery.common.ResourceNotFoundException.class)
                .hasMessage("Restaurant was not found");
    }
}
