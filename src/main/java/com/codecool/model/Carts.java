package com.codecool.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "carts")
@NamedQueries({
        @NamedQuery(name = "findAvailableCartsByUserId",
                query="select c from Carts c where c.user = :user and c.checkedOut = false"),
})
public class Carts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(targetEntity = Users.class)
    @JoinColumn
    @JsonIgnore
    private Users user;

    private Integer price;

    private boolean checkedOut;

    @OneToOne(targetEntity = Orders.class,  mappedBy = "cart")
    @JsonIgnore
    private Orders orders;

    @OneToMany(targetEntity = CartItems.class, mappedBy = "cart")
    @JsonIgnore
    private List<CartItems> cartItems = new ArrayList<>();

    public Carts(Users user, Integer price, boolean checkedOut) {
        this.user = user;
        this.price = price;
        this.checkedOut = checkedOut;
    }
}
