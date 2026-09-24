package com.vasylyna.fooddelivery.order;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {
    @EntityGraph(attributePaths = "address")
    List<CustomerOrder> findByUser_EmailOrderByCreatedAtDesc(String email);

    @EntityGraph(attributePaths = "address")
    Optional<CustomerOrder> findByIdAndUser_Email(Long id, String email);

    @Override
    @EntityGraph(attributePaths = "address")
    List<CustomerOrder> findAll();
}
