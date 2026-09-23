package com.vasylyna.fooddelivery.cart;
import com.vasylyna.fooddelivery.user.AppUser;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name="carts") public class Cart {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @OneToOne(fetch=FetchType.LAZY) @JoinColumn(name="user_id",nullable=false,unique=true) private AppUser user;
 @Column(name="updated_at",nullable=false) private Instant updatedAt=Instant.now();
 protected Cart(){} public Cart(AppUser user){this.user=user;}
 public Long getId(){return id;} public void touch(){updatedAt=Instant.now();}
}
