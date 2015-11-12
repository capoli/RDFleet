package com.realdolmen.rdfleet.domain;

import com.realdolmen.rdfleet.converters.LocalDatePersistenceConverter;
import com.realdolmen.rdfleet.validation.NoFutureDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.CascadeType;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity(name = "CarOrder")
public class Order extends BaseEntity {
    @NotNull
    @Convert(converter = LocalDatePersistenceConverter.class)
    @NoFutureDate
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateOrdered;
    @Min(0)
    @NotNull
    private BigDecimal amountPaidByEmployee;
    @Min(0)
    @NotNull
    private BigDecimal amountPaidByCompany;
    @NotNull
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private EmployeeCar orderedCar;
    @Convert(converter = LocalDatePersistenceConverter.class)
    @NoFutureDate
    @DateTimeFormat(pattern = "dd/MM/yyyy")
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
        if(amountPaidByCompany != null)
            this.amountPaidByCompany = amountPaidByCompany.setScale(2, BigDecimal.ROUND_HALF_UP);
        else
            this.amountPaidByCompany = null;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (dateOrdered != null ? !dateOrdered.equals(order.dateOrdered) : order.dateOrdered != null) return false;
        if (amountPaidByEmployee != null ? !amountPaidByEmployee.equals(order.amountPaidByEmployee) : order.amountPaidByEmployee != null)
            return false;
        if (amountPaidByCompany != null ? !amountPaidByCompany.equals(order.amountPaidByCompany) : order.amountPaidByCompany != null)
            return false;
        if (orderedCar != null ? !orderedCar.equals(order.orderedCar) : order.orderedCar != null) return false;
        if((dateReceived != null ? !dateReceived.equals(order.dateReceived) : order.dateReceived != null)) return false;
        return true;

    }

    @Override
    public int hashCode() {
        int result = dateOrdered != null ? dateOrdered.hashCode() : 0;
        result = 31 * result + (amountPaidByEmployee != null ? amountPaidByEmployee.hashCode() : 0);
        result = 31 * result + (amountPaidByCompany != null ? amountPaidByCompany.hashCode() : 0);
        result = 31 * result + (orderedCar != null ? orderedCar.hashCode() : 0);
        result = 31 * result + (dateReceived != null ? dateReceived.hashCode() : 0);
        return result;
    }
}
