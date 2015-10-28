package com.realdolmen.rdfleet.domain;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

/**
 * Created by JSTAX29 on 27/10/2015.
 */
@Entity
public class CarOption extends BaseEntity {
    @NotNull
    @NotBlank
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
