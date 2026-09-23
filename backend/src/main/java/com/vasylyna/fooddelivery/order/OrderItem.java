package com.vasylyna.fooddelivery.order;
import com.vasylyna.fooddelivery.menu.MenuItem;import jakarta.persistence.*;import java.math.BigDecimal;
@Entity @Table(name="order_items") public class OrderItem{
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="order_id",nullable=false) private CustomerOrder order;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="menu_item_id") private MenuItem menuItem;
 @Column(name="item_name",nullable=false,length=160) private String itemName;
 @Column(name="unit_price",nullable=false,precision=10,scale=2) private BigDecimal unitPrice;
 @Column(nullable=false) private int quantity;
 protected OrderItem(){}public OrderItem(CustomerOrder order,MenuItem item,String name,BigDecimal price,int quantity){this.order=order;this.menuItem=item;this.itemName=name;this.unitPrice=price;this.quantity=quantity;}
 public String getItemName(){return itemName;}public BigDecimal getUnitPrice(){return unitPrice;}public int getQuantity(){return quantity;}
}
