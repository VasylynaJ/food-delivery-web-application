package com.vasylyna.fooddelivery.cart;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CartItemRepository extends JpaRepository<CartItem,Long>{List<CartItem> findByCart_Id(Long id);Optional<CartItem> findByCart_IdAndMenuItem_Id(Long cartId,Long itemId);void deleteByCart_IdAndMenuItem_Id(Long cartId,Long itemId);void deleteByCart_Id(Long cartId);}
