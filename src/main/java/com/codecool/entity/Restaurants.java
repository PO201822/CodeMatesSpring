package com.codecool.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "restaurants")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@NamedQueries({
        @NamedQuery(name = "findsomething",
                query="select distinct r from Restaurants r inner join r.products p where p.name = :prodname"),
})
public class Restaurants {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String location;

    private Integer rating;

    private Integer max_cut;

    private String picture;

    private String description;

    @ManyToMany(mappedBy = "restaurants")
    @JsonIgnore
    private List<Products> products = new ArrayList<>();

    @OneToMany(mappedBy = "users")
    @JsonIgnore
    private List<Ratings> ratings = new ArrayList<>();
}

