package com.realdolmen.rdfleet.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JSTAX29 on 27/10/2015.
 */
@Entity
public class RdEmployee extends User {
    @Min(2)
    @Max(7)
    private int functionalLevel;
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Order currentOrder;
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Order> orderHistory = new ArrayList<>();
    private boolean inService;

    public int getFunctionalLevel() {
        return functionalLevel;
    }

    public void setFunctionalLevel(int functionalLevel) {
        this.functionalLevel = functionalLevel;
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }

    public List<Order> getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(List<Order> orderHistory) {
        this.orderHistory = orderHistory;
    }

    public boolean isInService() {
        return inService;
    }

    public void setInService(boolean inService) {
        this.inService = inService;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RdEmployee that = (RdEmployee) o;

        if(!super.equals(o)) return false;
        if (functionalLevel != that.functionalLevel) return false;
        if (inService != that.inService) return false;
        if ( currentOrder != null ? !currentOrder.equals(that.currentOrder) : that.currentOrder != null) return false;
        return true;

    }

    @Override
    public int hashCode() {
        int result = functionalLevel;
        result = 31 * result + (currentOrder != null ? currentOrder.hashCode() : 0);
        result = 31 * result + (orderHistory != null ? orderHistory.hashCode() : 0);
        result = 31 * result + (inService ? 1 : 0);
        return result;
    }
}
