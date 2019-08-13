package com.codecool.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Getter
@Setter
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
    private List<Users> courier = new ArrayList<>();

    @ManyToOne(targetEntity = Users.class)
    @JoinColumn
    private List<Users> user = new ArrayList<>();

    private boolean pickedUp;

    private boolean complete;

    @ManyToOne(targetEntity = Carts.class)
    @JoinColumn
    private List<Carts> cart = new ArrayList<>();
}
