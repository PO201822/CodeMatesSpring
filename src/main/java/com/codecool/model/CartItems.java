package com.codecool.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@NamedQueries({
        @NamedQuery(name = "findCartItemsByCartId",
                query="select distinct ci from CartItems ci inner join ci.cart c where c.id = :cart_id"),
        @NamedQuery(name = "deleteProductFromCart",
                query="delete from CartItems ci where ci.product = :product and ci.cart = :cart"),
})
public class CartItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(targetEntity = Carts.class)
    @JoinColumn
    @JsonIgnore
    private Carts cart;

    @ManyToOne(targetEntity = Products.class)
    @JoinColumn
    private Products product;

    private Integer quantity;

    private Integer price;

    public CartItems(Carts cart, Products product, Integer quantity, Integer price) {
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }
}
