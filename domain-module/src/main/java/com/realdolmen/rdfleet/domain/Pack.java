package com.realdolmen.rdfleet.domain;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JSTAX29 on 27/10/2015.
 */
@Entity
public class Pack extends BaseEntity {
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @ElementCollection
    private List<String> items = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }
}
