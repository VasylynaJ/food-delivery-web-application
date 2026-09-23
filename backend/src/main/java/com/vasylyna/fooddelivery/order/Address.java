package com.vasylyna.fooddelivery.order;
import com.vasylyna.fooddelivery.user.AppUser;
import jakarta.persistence.*;
@Entity @Table(name="addresses") public class Address{
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="user_id",nullable=false) private AppUser user;
 @Column(length=80) private String label;@Column(nullable=false,length=180) private String street;@Column(nullable=false,length=100) private String city;@Column(name="postal_code",nullable=false,length=20) private String postalCode;@Column(length=500) private String instructions;
 protected Address(){}public Address(AppUser user,String label,String street,String city,String postalCode,String instructions){this.user=user;this.label=label;this.street=street;this.city=city;this.postalCode=postalCode;this.instructions=instructions;}
 public Long getId(){return id;}public String getStreet(){return street;}public String getCity(){return city;}public String getPostalCode(){return postalCode;}public String getLabel(){return label;}
}
