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
public class Carts {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
}
