package com.realdolmen.rdfleet.domain;

import com.realdolmen.rdfleet.converters.LocalDatePersistenceConverter;

import javax.persistence.CascadeType;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by JSTAX29 on 28/10/2015.
 */
@Entity(name = "CarOrder")
public class Order extends BaseEntity {
    @NotNull
    @Convert(converter = LocalDatePersistenceConverter.class)
    private LocalDate dateOrdered;
    @Min(0)
    private BigDecimal amountPaidByEmployee;
    @Min(0)
    private BigDecimal amountPaidByCompany;
    @NotNull
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private EmployeeCar orderedCar;
    @Convert(converter = LocalDatePersistenceConverter.class)
    private LocalDate dateReceived;

    public LocalDate getDateOrdered() {
        return dateOrdered;
    }

    public void setDateOrdered(LocalDate dateOrdered) {
        this.dateOrdered = dateOrdered;
    }

    public BigDecimal getAmountPaidByEmployee() {
        return amountPaidByEmployee;
    }

    public void setAmountPaidByEmployee(BigDecimal amountPaidByEmployee) {
        this.amountPaidByEmployee = amountPaidByEmployee;
    }

    public BigDecimal getAmountPaidByCompany() {
        return amountPaidByCompany;
    }

    public void setAmountPaidByCompany(BigDecimal amountPaidByCompany) {
        this.amountPaidByCompany = amountPaidByCompany;
    }

    public EmployeeCar getOrderedCar() {
        return orderedCar;
    }

    public void setOrderedCar(EmployeeCar orderedCar) {
        this.orderedCar = orderedCar;
    }

    public LocalDate getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(LocalDate dateReceived) {
        this.dateReceived = dateReceived;
    }
}
