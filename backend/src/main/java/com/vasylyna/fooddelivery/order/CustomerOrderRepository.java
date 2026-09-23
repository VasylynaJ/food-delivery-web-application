package com.vasylyna.fooddelivery.order;
import java.util.*;import org.springframework.data.jpa.repository.JpaRepository;
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder,Long>{List<CustomerOrder> findByUser_EmailOrderByCreatedAtDesc(String email);Optional<CustomerOrder> findByIdAndUser_Email(Long id,String email);}
