package com.codecool.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItems {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToMany(targetEntity = Carts.class)
    @JoinColumn
    private List<Carts> cart = new ArrayList<>();

    @OneToMany(targetEntity = Products.class)
    @JoinColumn
    private List<Products> product = new ArrayList<>();

    private Integer quantity;

    private Integer price;

    @OneToMany(targetEntity = Orders.class)
    private List<Orders> orders = new ArrayList<>();


}
