package com.vasylyna.fooddelivery.restaurant;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "restaurants")
public class Restaurant {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 160) private String name;
    @Column(columnDefinition = "text") private String description;
    @Column(nullable = false, length = 80) private String cuisine;
    @Column(nullable = false, precision = 2, scale = 1) private BigDecimal rating = BigDecimal.ZERO;
    @Column(name = "delivery_fee", nullable = false, precision = 10, scale = 2) private BigDecimal deliveryFee = BigDecimal.ZERO;
    @Column(name = "opening_hours", nullable = false, length = 120) private String openingHours;
    @Column(name = "image_url", columnDefinition = "text") private String imageUrl;
    @Column(name = "is_open", nullable = false) private boolean open = true;

    protected Restaurant() {}
    public Restaurant(String name, String description, String cuisine, BigDecimal rating, BigDecimal deliveryFee, String openingHours, String imageUrl, boolean open) {
        this.name = name; this.description = description; this.cuisine = cuisine; this.rating = rating; this.deliveryFee = deliveryFee; this.openingHours = openingHours; this.imageUrl = imageUrl; this.open = open;
    }
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getCuisine() { return cuisine; }
    public BigDecimal getRating() { return rating; }
    public BigDecimal getDeliveryFee() { return deliveryFee; }
    public String getOpeningHours() { return openingHours; }
    public String getImageUrl() { return imageUrl; }
    public boolean isOpen() { return open; }
    public void update(String name,String description,String cuisine,BigDecimal rating,BigDecimal deliveryFee,String openingHours,String imageUrl,boolean open) { this.name=name;this.description=description;this.cuisine=cuisine;this.rating=rating;this.deliveryFee=deliveryFee;this.openingHours=openingHours;this.imageUrl=imageUrl;this.open=open; }
}
