package com.realdolmen.rdfleet.domain;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by JSTAX29 on 27/10/2015.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class User extends BaseEntity {
    @Column(nullable = false)
    @NotNull
    @NotBlank
    private String firstName;
    @Column(nullable = false)
    @NotNull
    @NotBlank
    private String lastName;
    @Column(nullable = false, unique = true)
    @NotNull
    @NotBlank
    @Email
    private String email;
    @Column(nullable = false)
    @NotNull
    @NotBlank
    private String password;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
