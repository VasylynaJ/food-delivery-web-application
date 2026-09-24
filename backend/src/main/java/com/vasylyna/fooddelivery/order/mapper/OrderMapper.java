package com.vasylyna.fooddelivery.order.mapper;

import com.vasylyna.fooddelivery.order.CustomerOrder;
import com.vasylyna.fooddelivery.order.OrderItem;
import com.vasylyna.fooddelivery.order.dto.OrderItemResponse;
import com.vasylyna.fooddelivery.order.dto.OrderResponse;
import java.util.List;

public final class OrderMapper {
    private OrderMapper() {
    }

    public static OrderResponse from(CustomerOrder order, List<OrderItem> orderItems) {
        List<OrderItemResponse> items = orderItems.stream()
                .map(item -> new OrderItemResponse(item.getItemName(), item.getUnitPrice(), item.getQuantity()))
                .toList();
        return new OrderResponse(order.getId(), order.getStatus().name(), order.getSubtotal(), order.getDeliveryFee(),
                order.getTotal(), order.getCreatedAt(), order.getAddress().getStreet(), order.getAddress().getCity(),
                order.getAddress().getPostalCode(), items);
    }
}
