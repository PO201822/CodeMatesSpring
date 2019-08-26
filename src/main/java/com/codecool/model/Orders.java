package com.codecool.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Getter
@NoArgsConstructor
@AllArgsConstructor
@NamedQueries({
        @NamedQuery(name = "findOrders",
                query="select o from Orders o join o.user user where user.name = :username"),
})
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(targetEntity = Users.class)
    @JoinColumn
    @JsonBackReference
    private Users courier;

    @ManyToOne(targetEntity = Users.class)
    @JoinColumn
    @JsonBackReference
    private Users user;

    private boolean pickedUp;

    private boolean complete;

    @OneToOne(targetEntity = Carts.class)
    @JoinColumn
    @JsonManagedReference
    private Carts cart;

}