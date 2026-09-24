package com.vasylyna.fooddelivery.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.vasylyna.fooddelivery.common.BusinessRuleException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.vasylyna.fooddelivery.order.dto.OrderResponse;
import com.vasylyna.fooddelivery.user.AppUser;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class OrderStatusTransitionTest {
    @Test
    void acceptsExactlyTheExistingAllowedTransitions() {
        assertAllowed(OrderStatus.PENDING, OrderStatus.CONFIRMED);
        assertAllowed(OrderStatus.PENDING, OrderStatus.CANCELLED);
        assertAllowed(OrderStatus.CONFIRMED, OrderStatus.PREPARING);
        assertAllowed(OrderStatus.CONFIRMED, OrderStatus.CANCELLED);
        assertAllowed(OrderStatus.PREPARING, OrderStatus.OUT_FOR_DELIVERY);
        assertAllowed(OrderStatus.PREPARING, OrderStatus.CANCELLED);
        assertAllowed(OrderStatus.OUT_FOR_DELIVERY, OrderStatus.DELIVERED);
    }

    @Test
    void rejectsDisallowedTransitionsWithTheExistingErrorMessage() {
        assertRejected(OrderStatus.PENDING, OrderStatus.PREPARING);
        assertRejected(OrderStatus.CONFIRMED, OrderStatus.DELIVERED);
        assertRejected(OrderStatus.PREPARING, OrderStatus.CONFIRMED);
        assertRejected(OrderStatus.OUT_FOR_DELIVERY, OrderStatus.CANCELLED);
        assertRejected(OrderStatus.DELIVERED, OrderStatus.CANCELLED);
        assertRejected(OrderStatus.CANCELLED, OrderStatus.CONFIRMED);
    }

    private void assertAllowed(OrderStatus current, OrderStatus next) {
        CustomerOrder order = orderWithStatus(current);
        OrderService service = serviceFor(order);

        OrderResponse response = service.status(1L, next);

        assertThat(order.getStatus()).isEqualTo(next);
        assertThat(response.status()).isEqualTo(next.name());
    }

    private void assertRejected(OrderStatus current, OrderStatus next) {
        CustomerOrder order = orderWithStatus(current);
        OrderService service = serviceFor(order);

        assertThatThrownBy(() -> service.status(1L, next))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("Order cannot move from " + current + " to " + next);
        assertThat(order.getStatus()).isEqualTo(current);
    }

    private CustomerOrder orderWithStatus(OrderStatus status) {
        CustomerOrder order = new CustomerOrder(mock(AppUser.class), mock(Address.class), BigDecimal.ONE,
                BigDecimal.ZERO, BigDecimal.ONE);
        order.setStatus(status);
        return order;
    }

    private OrderService serviceFor(CustomerOrder order) {
        CustomerOrderRepository orders = mock(CustomerOrderRepository.class);
        when(orders.findById(1L)).thenReturn(Optional.of(order));
        OrderItemRepository orderItems = mock(OrderItemRepository.class);
        when(orderItems.findByOrder_Id(null)).thenReturn(List.of());
        when(orderItems.findByOrder_Id(1L)).thenReturn(List.of());
        return new OrderService(mock(com.vasylyna.fooddelivery.cart.CartRepository.class),
                mock(com.vasylyna.fooddelivery.cart.CartItemRepository.class), orders, orderItems,
                mock(AddressRepository.class), mock(com.vasylyna.fooddelivery.user.AppUserRepository.class));
    }
}