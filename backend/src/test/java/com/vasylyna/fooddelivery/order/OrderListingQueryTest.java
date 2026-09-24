package com.vasylyna.fooddelivery.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.vasylyna.fooddelivery.cart.CartItemRepository;
import com.vasylyna.fooddelivery.cart.CartRepository;
import com.vasylyna.fooddelivery.order.dto.OrderResponse;
import com.vasylyna.fooddelivery.user.AppUserRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;

class OrderListingQueryTest {
    @Test
    void historyBulkLoadsOrderItemsAndRetainsRepositoryOrder() {
        CustomerOrder first = order(1L);
        CustomerOrder second = order(2L);
        CustomerOrderRepository orders = mock(CustomerOrderRepository.class);
        when(orders.findByUser_EmailOrderByCreatedAtDesc("person@example.com"))
                .thenReturn(List.of(first, second));
        OrderItemRepository items = mock(OrderItemRepository.class);
        when(items.findByOrder_IdIn(List.of(1L, 2L))).thenReturn(List.of());

        List<OrderResponse> response = service(orders, items).history("person@example.com");

        assertThat(response).extracting(OrderResponse::id).containsExactly(1L, 2L);
        verify(items).findByOrder_IdIn(List.of(1L, 2L));
        verify(items, never()).findByOrder_Id(anyLong());
    }

    private CustomerOrder order(Long id) {
        Address address = mock(Address.class);
        when(address.getStreet()).thenReturn("Street");
        when(address.getCity()).thenReturn("City");
        when(address.getPostalCode()).thenReturn("10000");
        CustomerOrder order = mock(CustomerOrder.class);
        when(order.getId()).thenReturn(id);
        when(order.getAddress()).thenReturn(address);
        when(order.getStatus()).thenReturn(OrderStatus.PENDING);
        when(order.getSubtotal()).thenReturn(BigDecimal.ZERO);
        when(order.getDeliveryFee()).thenReturn(BigDecimal.ZERO);
        when(order.getTotal()).thenReturn(BigDecimal.ZERO);
        when(order.getCreatedAt()).thenReturn(Instant.EPOCH);
        return order;
    }

    private OrderService service(CustomerOrderRepository orders, OrderItemRepository items) {
        return new OrderService(mock(CartRepository.class), mock(CartItemRepository.class), orders, items,
                mock(AddressRepository.class), mock(AppUserRepository.class));
    }
}