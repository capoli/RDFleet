package com.realdolmen.rdfleet.domain;

import com.realdolmen.rdfleet.converters.LocalDatePersistenceConverter;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
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
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private List<CarOption> carOptions = new ArrayList<>();
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @NotNull
    private Car selectedCar;
    @NotNull
    @NotBlank
    @Column(unique = true)
    private String licensePlate;

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

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = (licensePlate != null ? licensePlate.toUpperCase() : null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmployeeCar that = (EmployeeCar) o;

        if (mileage != that.mileage) return false;
        if (carStatus != that.carStatus) return false;
//        if (carOptions != null ? !carOptions.equals(that.carOptions) : that.carOptions != null) return false;
        if (selectedCar != null ? !selectedCar.equals(that.selectedCar) : that.selectedCar != null) return false;
        return !(licensePlate != null ? !licensePlate.equals(that.licensePlate) : that.licensePlate != null);

    }

    @Override
    public int hashCode() {
        int result = mileage;
        result = 31 * result + (carStatus != null ? carStatus.hashCode() : 0);
        result = 31 * result + (carOptions != null ? carOptions.hashCode() : 0);
        result = 31 * result + (selectedCar != null ? selectedCar.hashCode() : 0);
        result = 31 * result + (licensePlate != null ? licensePlate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EmployeeCar{" +
                "mileage=" + mileage +
                ", carStatus=" + carStatus +
                ", carOptions=" + carOptions +
                ", selectedCar=" + selectedCar +
                ", licensePlate='" + licensePlate + '\'' +
                '}';
    }
}
