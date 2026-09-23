package com.vasylyna.fooddelivery.cart;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
public interface CartRepository extends JpaRepository<Cart,Long>{@Query("select c from Cart c join fetch c.user where c.user.email = :email") Optional<Cart> findByEmail(@Param("email") String email);}
