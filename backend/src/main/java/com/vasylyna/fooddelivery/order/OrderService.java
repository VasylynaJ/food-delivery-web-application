package com.vasylyna.fooddelivery.order;

import com.vasylyna.fooddelivery.cart.Cart;
import com.vasylyna.fooddelivery.cart.CartItem;
import com.vasylyna.fooddelivery.cart.CartItemRepository;
import com.vasylyna.fooddelivery.cart.CartRepository;
import com.vasylyna.fooddelivery.cart.CartService;
import com.vasylyna.fooddelivery.common.BusinessRuleException;
import com.vasylyna.fooddelivery.common.ResourceNotFoundException;
import com.vasylyna.fooddelivery.order.dto.CheckoutRequest;
import com.vasylyna.fooddelivery.order.dto.OrderResponse;
import com.vasylyna.fooddelivery.order.mapper.OrderMapper;
import com.vasylyna.fooddelivery.user.AppUser;
import com.vasylyna.fooddelivery.user.AppUserRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final CartRepository carts;
    private final CartItemRepository cartItems;
    private final CustomerOrderRepository orders;
    private final OrderItemRepository orderItems;
    private final AddressRepository addresses;
    private final AppUserRepository users;

    public OrderService(CartRepository carts, CartItemRepository cartItems, CustomerOrderRepository orders,
            OrderItemRepository orderItems, AddressRepository addresses, AppUserRepository users) {
        this.carts = carts;
        this.cartItems = cartItems;
        this.orders = orders;
        this.orderItems = orderItems;
        this.addresses = addresses;
        this.users = users;
    }

    @Transactional
    public OrderResponse checkout(String email, CheckoutRequest request) {
        AppUser user = users.findByEmailIgnoreCase(email).orElseThrow();
        Cart cart = carts.findByEmail(email)
                .orElseThrow(() -> new BusinessRuleException("Your cart is empty"));
        var lines = cartItems.findByCart_Id(cart.getId());
        if (lines.isEmpty()) {
            throw new BusinessRuleException("Your cart is empty");
        }
        if (lines.stream().anyMatch(item -> !item.getMenuItem().isAvailable())) {
            throw new BusinessRuleException("One or more items in your cart are no longer available");
        }
        var subtotal = CartService.subtotalFor(lines);
        BigDecimal fee = CartService.deliveryFeeFor(lines);
        Address address = addresses.save(new Address(user, request.label(), request.street(), request.city(),
                request.postalCode(), request.instructions()));
        CustomerOrder order = orders.save(new CustomerOrder(user, address, subtotal, fee, subtotal.add(fee)));
        for (CartItem line : lines) {
            orderItems.save(new OrderItem(order, line.getMenuItem(), line.getMenuItem().getName(),
                    line.getMenuItem().getPrice(), line.getQuantity()));
        }
        cartItems.deleteByCart_Id(cart.getId());
        return view(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> history(String email) {
        return views(orders.findByUser_EmailOrderByCreatedAtDesc(email));
    }

    @Transactional(readOnly = true)
    public OrderResponse get(String email, Long id) {
        return view(orders.findByIdAndUser_Email(id, email)
                .orElseThrow(() -> new ResourceNotFoundException("Order was not found")));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> all() {
        return views(orders.findAll());
    }

    @Transactional
    public OrderResponse status(Long id, OrderStatus status) {
        CustomerOrder order = orders.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order was not found"));
        boolean allowed = switch (order.getStatus()) {
            case PENDING -> status == OrderStatus.CONFIRMED || status == OrderStatus.CANCELLED;
            case CONFIRMED -> status == OrderStatus.PREPARING || status == OrderStatus.CANCELLED;
            case PREPARING -> status == OrderStatus.OUT_FOR_DELIVERY || status == OrderStatus.CANCELLED;
            case OUT_FOR_DELIVERY -> status == OrderStatus.DELIVERED;
            case DELIVERED, CANCELLED -> false;
        };
        if (!allowed) {
            throw new BusinessRuleException("Order cannot move from " + order.getStatus() + " to " + status);
        }
        order.setStatus(status);
        return view(order);
    }

    private OrderResponse view(CustomerOrder order) {
        return OrderMapper.from(order, orderItems.findByOrder_Id(order.getId()));
    }

    private List<OrderResponse> views(List<CustomerOrder> orderList) {
        if (orderList.isEmpty()) {
            return List.of();
        }
        var orderIds = orderList.stream().map(CustomerOrder::getId).toList();
        Map<Long, List<OrderItem>> itemsByOrder = orderItems.findByOrder_IdIn(orderIds).stream()
                .collect(Collectors.groupingBy(OrderItem::getOrderId));
        return orderList.stream()
                .map(order -> OrderMapper.from(order, itemsByOrder.getOrDefault(order.getId(), List.of())))
                .toList();
    }
}
