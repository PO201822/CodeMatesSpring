package com.codecool.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQueries({
        @NamedQuery(name = "findRatedRestaurants",
                query="select distinct r from Restaurants r inner join r.ratings rate where rate.user_id = :iduser"),
       })
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    private String email;

    private String password;

    private String location;

    private String address;

    @Type(type = "com.codecool.util.JPAArrayHandler")
    public String[] roles;

    /*
    @OneToMany(mappedBy = "users")
    private List<Ratings> ratings = new ArrayList<>();

    @OneToMany(targetEntity = Carts.class, mappedBy = "user")
    private List<Carts> carts = new ArrayList<>();

    @OneToMany(targetEntity = Orders.class, mappedBy = "user")
    private List<Orders> orders = new ArrayList<>();

    @OneToMany(targetEntity = Orders.class, mappedBy = "user")
    private List<Orders> couriers = new ArrayList<>();
    */

    public Users(String name, String email, String password, String location, String address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.location = location;
        this.address = address;
    }
}
