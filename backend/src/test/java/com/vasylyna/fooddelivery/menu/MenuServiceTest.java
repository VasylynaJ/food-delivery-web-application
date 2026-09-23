package com.vasylyna.fooddelivery.menu;
import static org.assertj.core.api.Assertions.assertThat;import static org.mockito.Mockito.*;
import java.util.List;import org.junit.jupiter.api.Test;
class MenuServiceTest{
 @Test void menuListsOnlyAvailableRowsForSelectedRestaurant(){MenuItemRepository repo=mock(MenuItemRepository.class);when(repo.findByCategory_Restaurant_IdAndAvailableTrueOrderByCategory_NameAscNameAsc(12L)).thenReturn(List.of());assertThat(new MenuService(repo).list(12L)).isEmpty();verify(repo).findByCategory_Restaurant_IdAndAvailableTrueOrderByCategory_NameAscNameAsc(12L);}
}
