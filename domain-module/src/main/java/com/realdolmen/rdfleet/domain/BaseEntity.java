package com.realdolmen.rdfleet.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by JSTAX29 on 27/10/2015.
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Version
    private Long version;

    public Long getId() {
        return id;
    }


    public Long getVersion() {
        return version;
    }
}
