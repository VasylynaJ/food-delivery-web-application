package com.vasylyna.fooddelivery.order;
import com.vasylyna.fooddelivery.user.AppUser;
import jakarta.persistence.*;
import java.math.BigDecimal;import java.time.Instant;
@Entity @Table(name="customer_orders") public class CustomerOrder{
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="user_id",nullable=false) private AppUser user;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="address_id",nullable=false) private Address address;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=30) private OrderStatus status=OrderStatus.PENDING;
 @Column(nullable=false,precision=10,scale=2) private BigDecimal subtotal;
 @Column(name="delivery_fee",nullable=false,precision=10,scale=2) private BigDecimal deliveryFee;
 @Column(nullable=false,precision=10,scale=2) private BigDecimal total;
 @Column(name="created_at",nullable=false) private Instant createdAt=Instant.now();
 protected CustomerOrder(){}public CustomerOrder(AppUser user,Address address,BigDecimal subtotal,BigDecimal fee,BigDecimal total){this.user=user;this.address=address;this.subtotal=subtotal;this.deliveryFee=fee;this.total=total;}
 public Long getId(){return id;}public AppUser getUser(){return user;}public Address getAddress(){return address;}public OrderStatus getStatus(){return status;}public BigDecimal getSubtotal(){return subtotal;}public BigDecimal getDeliveryFee(){return deliveryFee;}public BigDecimal getTotal(){return total;}public Instant getCreatedAt(){return createdAt;}public void setStatus(OrderStatus status){this.status=status;}
}
