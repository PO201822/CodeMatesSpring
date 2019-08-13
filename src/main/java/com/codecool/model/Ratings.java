package com.codecool.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(RatingId.class)
public class Ratings implements Serializable {

    @EmbeddedId
    private RatingId ratingId;

    @Id
    private Integer restaurant_id;
    @Id
    private Integer user_id;

    @ManyToOne(targetEntity = Restaurants.class)
    @MapsId("restaurant_id")
    @JoinColumn(name = "restaurant_id", updatable = false, insertable = false,
            referencedColumnName = "id")
    private List<Restaurants> restaurants = new ArrayList<>();

    @ManyToOne(targetEntity = Users.class)
    @MapsId("user_id")
    @JoinColumn(name = "user_id", updatable = false, insertable = false,
            referencedColumnName = "id")
    private List<Users> users = new ArrayList<>();

    private Integer rating;
}
