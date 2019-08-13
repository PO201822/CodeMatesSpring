package com.codecool.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    private String location;

    private Integer rating;

    private Integer max_cut;

    @ManyToMany(mappedBy = "restaurants")
    @JsonBackReference
    private List<Products> products = new ArrayList<>();

    @OneToMany(mappedBy = "users")
    @JsonBackReference
    private List<Ratings> ratings = new ArrayList<>();
}

