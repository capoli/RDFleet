package com.realdolmen.rdfleet.viewmodel;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by OCPAX79 on 3/11/2015.
 */
public class OrderNewCar implements Serializable {
    private Long id;

    private BigDecimal priceDowngrade;

    private BigDecimal priceUpgrade;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPriceDowngrade() {
        return priceDowngrade;
    }

    public void setPriceDowngrade(BigDecimal priceDowngrade) {
        this.priceDowngrade = priceDowngrade;
    }

    public BigDecimal getPriceUpgrade() {
        return priceUpgrade;
    }

    public void setPriceUpgrade(BigDecimal priceUpgrade) {
        this.priceUpgrade = priceUpgrade;
    }
}
