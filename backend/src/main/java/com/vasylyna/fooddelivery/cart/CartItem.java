package com.vasylyna.fooddelivery.cart;
import com.vasylyna.fooddelivery.menu.MenuItem;
import jakarta.persistence.*;
@Entity @Table(name="cart_items",uniqueConstraints=@UniqueConstraint(columnNames={"cart_id","menu_item_id"})) public class CartItem {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="cart_id",nullable=false) private Cart cart;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="menu_item_id",nullable=false) private MenuItem menuItem;
 @Column(nullable=false) private int quantity;
 protected CartItem(){} public CartItem(Cart cart,MenuItem menuItem,int quantity){this.cart=cart;this.menuItem=menuItem;this.quantity=quantity;}
 public Long getId(){return id;} public MenuItem getMenuItem(){return menuItem;} public int getQuantity(){return quantity;} public void setQuantity(int quantity){this.quantity=quantity;}
}
