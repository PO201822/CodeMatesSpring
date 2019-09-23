package com.codecool.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
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
                query = "select c from Carts c where c.user = :user and c.checkedOut = false"),
})
public class Carts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(targetEntity = Users.class)
    @JoinColumn
    @JsonIgnore
    private Users user;

    private Integer price;

    private boolean checkedOut;

    private LocalDateTime checkout_date;

    @OneToOne(targetEntity = Orders.class, mappedBy = "cart")
    @JsonIgnore
    private Orders orders;

    @OneToMany(targetEntity = CartItems.class, mappedBy = "cart")
    @JsonIgnore
    private List<CartItems> cartItems = new ArrayList<>();

    private boolean pickedup;

    public Carts(Users user, Integer price, boolean checkedOut) {
        this.user = user;
        this.price = price;
        this.checkedOut = checkedOut;
    }
}
