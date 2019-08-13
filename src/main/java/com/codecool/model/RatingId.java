package com.codecool.model;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class RatingId implements Serializable {

    private Integer restaurant_id;
    private Integer user_id;
}
