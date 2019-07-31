package com.codecool.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String username;

    private String email;

    private String password;

    private boolean role;
}
