package com.codecool.model;

import lombok.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    private Integer price;

    private boolean availability;

    private String picture;

    private String description;

    private String category;

    @ManyToMany
    @JoinTable(name="menus",
            joinColumns=@JoinColumn(name="restaurant_id"),
            inverseJoinColumns=@JoinColumn(name="product_id"))
    private List<Restaurants> restaurants = new ArrayList<>();

    @ManyToMany(targetEntity = CartItems.class)
    @JoinColumn
    private List<CartItems> cartItems = new ArrayList<>();
}
