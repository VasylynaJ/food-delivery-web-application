package com.vasylyna.fooddelivery.order;

import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrder_Id(Long id);

    List<OrderItem> findByOrder_IdIn(Collection<Long> orderIds);
}
