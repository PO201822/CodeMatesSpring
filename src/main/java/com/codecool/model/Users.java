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

    public Users(String name, String email, String password, String location, String address) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.location = location;
        this.address = address;
    }
}
