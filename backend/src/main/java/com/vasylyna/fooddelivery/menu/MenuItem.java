package com.vasylyna.fooddelivery.menu;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity @Table(name="menu_items")
public class MenuItem {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="category_id", nullable=false) private Category category;
    @Column(nullable=false, length=160) private String name;
    @Column(columnDefinition="text") private String description;
    @Column(nullable=false, precision=10, scale=2) private BigDecimal price;
    @Column(name="image_url", columnDefinition="text") private String imageUrl;
    @Column(nullable=false) private boolean available = true;
    protected MenuItem() {}
    public MenuItem(Category category,String name,String description,BigDecimal price,String imageUrl,boolean available){this.category=category;this.name=name;this.description=description;this.price=price;this.imageUrl=imageUrl;this.available=available;}
    public Long getId(){return id;} public Category getCategory(){return category;} public String getName(){return name;} public String getDescription(){return description;} public BigDecimal getPrice(){return price;} public String getImageUrl(){return imageUrl;} public boolean isAvailable(){return available;}
    public void update(Category category,String name,String description,BigDecimal price,String imageUrl,boolean available){this.category=category;this.name=name;this.description=description;this.price=price;this.imageUrl=imageUrl;this.available=available;}
}
