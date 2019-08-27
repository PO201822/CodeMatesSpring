package com.codecool.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Getter
@Setter
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
    @JsonIgnore
    @JoinColumn(name = "restaurant_id", updatable = false, insertable = false,
            referencedColumnName = "id")
    private Restaurants restaurants;

    @ManyToOne(targetEntity = Users.class)
    @MapsId("user_id")
    @JsonIgnore
    @JoinColumn(name = "user_id", updatable = false, insertable = false,
            referencedColumnName = "id")
    private Users users;

    private Integer rating;
}
