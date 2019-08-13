package com.codecool.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
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
