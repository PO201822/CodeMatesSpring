package com.codecool.model;

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
public class Carts {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(targetEntity = Users.class)
    @JoinColumn
    private Users user;

    private Integer price;

    private boolean checkedOut;

    @OneToMany(targetEntity = Orders.class,  mappedBy = "cart")
    private List<Orders> orders = new ArrayList<>();
}
