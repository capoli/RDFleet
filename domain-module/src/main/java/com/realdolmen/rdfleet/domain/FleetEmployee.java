package com.realdolmen.rdfleet.domain;

import javax.persistence.Entity;

@Entity
public class FleetEmployee extends RdEmployee {

    public FleetEmployee() {
        role = Role.ROLE_FLEETEMPLOYEE;
    }
}
