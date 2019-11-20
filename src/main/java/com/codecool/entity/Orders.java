package com.codecool.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@NamedQueries({
        @NamedQuery(name = "findOrders",
                query = "select o from Orders o join o.user user where user.name = :username"),
})
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(targetEntity = Users.class)
    @JoinColumn
    @JsonIgnore
    private Users courier;

    @ManyToOne(targetEntity = Users.class)
    @JoinColumn
    @JsonIgnore
    private Users user;

    private boolean complete;

    @OneToOne(targetEntity = Carts.class)
    @JoinColumn
    @JsonIgnore
    private Carts cart;

    private LocalDateTime complition_date;

    public Orders(Users courier, Users user, boolean complete, Carts cart) {
        this.courier = courier;
        this.user = user;
        this.complete = complete;
        this.cart = cart;
    }
}