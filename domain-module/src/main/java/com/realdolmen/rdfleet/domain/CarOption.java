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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CarOption carOption = (CarOption) o;

        return !(description != null ? !description.equals(carOption.description) : carOption.description != null);

    }

    @Override
    public int hashCode() {
        return description != null ? description.hashCode() : 0;
    }
}
