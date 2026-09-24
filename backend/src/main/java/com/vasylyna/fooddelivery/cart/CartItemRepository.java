package com.vasylyna.fooddelivery.cart;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @EntityGraph(attributePaths = {"menuItem", "menuItem.category", "menuItem.category.restaurant"})
    List<CartItem> findByCart_Id(Long id);

    Optional<CartItem> findByCart_IdAndMenuItem_Id(Long cartId, Long itemId);

    void deleteByCart_IdAndMenuItem_Id(Long cartId, Long itemId);

    void deleteByCart_Id(Long cartId);
}