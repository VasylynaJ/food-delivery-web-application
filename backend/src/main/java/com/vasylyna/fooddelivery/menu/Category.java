package com.vasylyna.fooddelivery.menu;

import com.vasylyna.fooddelivery.restaurant.Restaurant;
import jakarta.persistence.*;

@Entity @Table(name="categories", uniqueConstraints=@UniqueConstraint(columnNames={"restaurant_id","name"}))
public class Category {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="restaurant_id", nullable=false) private Restaurant restaurant;
    @Column(nullable=false, length=100) private String name;
    protected Category() {}
    public Category(Restaurant restaurant,String name){this.restaurant=restaurant;this.name=name;}
    public Long getId(){return id;} public String getName(){return name;} public Long getRestaurantId(){return restaurant.getId();}
    public Restaurant getRestaurant(){return restaurant;}
}
