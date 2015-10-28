package com.realdolmen.rdfleet.domain;

import com.realdolmen.rdfleet.converters.LocalDatePersistenceConverter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by JSTAX29 on 27/10/2015.
 */
@Entity
public class EmployeeCar extends BaseEntity {
    @Min(0)
    private int mileage;
    @NotNull
    @Enumerated(EnumType.STRING)
    private CarStatus carStatus;
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<CarOption> carOptions;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @NotNull
    private Car selectedCar;

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public CarStatus getCarStatus() {
        return carStatus;
    }

    public void setCarStatus(CarStatus carStatus) {
        this.carStatus = carStatus;
    }

    public List<CarOption> getCarOptions() {
        return carOptions;
    }

    public void setCarOptions(List<CarOption> carOptions) {
        this.carOptions = carOptions;
    }

    public Car getSelectedCar() {
        return selectedCar;
    }

    public void setSelectedCar(Car selectedCar) {
        this.selectedCar = selectedCar;
    }
}
