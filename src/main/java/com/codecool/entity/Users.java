package com.codecool.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Type;

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
        @NamedQuery(name = "findRatedRestaurants",
                query = "select distinct r from Restaurants r inner join r.ratings rate where rate.user_id = :iduser"),
})
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String email;

    private String password;

    private String location;

    private String address;

    @Type(type = "com.codecool.util.JPAArrayHandler")
    private String[] roles;

    private int cut;

    private boolean premium;

    private int profit;

    @OneToMany(mappedBy = "users")
    @JsonIgnore
    private List<Ratings> ratings = new ArrayList<>();

    @OneToMany(targetEntity = Carts.class, mappedBy = "user")
    @JsonIgnore
    private List<Carts> carts = new ArrayList<>();

    @OneToMany(targetEntity = Orders.class, mappedBy = "user")
    @JsonIgnore
    private List<Orders> orders = new ArrayList<>();

    @JsonIgnore
    @OneToMany(targetEntity = Orders.class, mappedBy = "user")
    private List<Orders> couriers = new ArrayList<>();


    public Users(String name, String email, String password, String location, String address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.location = location;
        this.address = address;
    }

    public Users(String name, String location, String address) {
        this.name = name;
        this.location = location;
        this.address = address;
    }
}
