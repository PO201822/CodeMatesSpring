package com.codecool.model;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;
@Data
@Embeddable
public class RatingId implements Serializable {

    private Integer restaurant_id;
    private Integer user_id;
}
