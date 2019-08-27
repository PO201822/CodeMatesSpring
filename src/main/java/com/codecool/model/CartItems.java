package com.codecool.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItems {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(targetEntity = Carts.class)
    @JoinColumn
    private Carts cart;

    @OneToMany(targetEntity = Products.class)
    @JoinColumn
    private List<Products> product = new ArrayList<>();

    private Integer quantity;

    private Integer price;

    @OneToMany(targetEntity = Orders.class)
    private List<Orders> orders = new ArrayList<>();


}
