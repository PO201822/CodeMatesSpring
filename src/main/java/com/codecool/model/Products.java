package com.codecool.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
@Data
@Builder
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
    @JsonManagedReference
    @JoinTable(name="menus",
            joinColumns=@JoinColumn(name="restaurant_id"),
            inverseJoinColumns=@JoinColumn(name="product_id"))
    private List<Restaurants> restaurants;
}
