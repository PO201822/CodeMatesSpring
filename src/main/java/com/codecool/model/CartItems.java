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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(targetEntity = Carts.class)
    @JoinColumn
    private Carts cart;

    @ManyToOne(targetEntity = Products.class)
    @JoinColumn
    private Products product;

    private Integer quantity;

    private Integer price;

    @OneToMany(targetEntity = Orders.class)
    private List<Orders> orders = new ArrayList<>();


    public CartItems(Carts cart, Products product, Integer quantity, Integer price) {
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }
}
