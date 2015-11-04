package com.realdolmen.rdfleet.domain;

import com.realdolmen.rdfleet.converters.DurationPersistenceConverter;
import com.realdolmen.rdfleet.validation.PositiveDuration;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JSTAX29 on 27/10/2015.
 */
@Entity
public class Car extends BaseEntity {
    @NotNull
    @NotBlank
    private String make;
    @NotNull
    @NotBlank
    private String model;
    @Min(2)
    @Max(7)
    private int functionalLevel;
    private String description;
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private List<Pack> packs = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    @NotNull
    private TyreType tyreType;
    @Enumerated(EnumType.STRING)
    @NotNull
    private CarType carType = CarType.REGULAR;
    @Enumerated(EnumType.STRING)
    @NotNull
    private FuelType fuelType;
    private boolean towingBracketPossibility;
    private boolean gps;
    private boolean bluetooth;
    @Min(0)
    private int idealKm;
    @Min(0)
    private int maxKm;
    @NotNull
    @Min(value = 0)
    @Digits(fraction = 2, integer = 15)
    private BigDecimal listPrice;
    @NotNull
    @Min(value = 0)
    @Digits(fraction = 2, integer = 8)
    private BigDecimal benefit;
    @NotNull
    @Min(value = 0)
    @Digits(fraction = 2, integer = 8)
    private BigDecimal amountUpgrade;
    @NotNull
    @Min(value = 0)
    @Digits(fraction = 2, integer = 8)
    private BigDecimal amountDowngrade;
    @Min(value = 0)
    private int co2;
    @Min(value = 0)
    private int fiscHp;
    @NotNull
    @Convert(converter = DurationPersistenceConverter.class)
    @PositiveDuration
    private Duration timeOfDeliveryInDays;

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getFunctionalLevel() {
        return functionalLevel;
    }

    public void setFunctionalLevel(int functionalLevel) {
        this.functionalLevel = functionalLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Pack> getPacks() {
        return packs;
    }

    public void setPacks(List<Pack> packs) {
        this.packs = packs;
    }

    public TyreType getTyreType() {
        return tyreType;
    }

    public void setTyreType(TyreType tyreType) {
        this.tyreType = tyreType;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public CarType getCarType() {
        return carType;
    }

    public void setCarType(CarType carType) {
        this.carType = carType;
    }

    public BigDecimal getBenefit() {
        return benefit;
    }

    public void setBenefit(BigDecimal benefit) {
        this.benefit = benefit;
    }

    public boolean isTowingBracketPossibility() {
        return towingBracketPossibility;
    }

    public void setTowingBracketPossibility(boolean towingBracketPossibility) {
        this.towingBracketPossibility = towingBracketPossibility;
    }

    public boolean isGps() {
        return gps;
    }

    public void setGps(boolean gps) {
        this.gps = gps;
    }

    public boolean isBluetooth() {
        return bluetooth;
    }

    public void setBluetooth(boolean bluetooth) {
        this.bluetooth = bluetooth;
    }

    public int getIdealKm() {
        return idealKm;
    }

    public void setIdealKm(int idealKm) {
        this.idealKm = idealKm;
    }

    public int getMaxKm() {
        return maxKm;
    }

    public void setMaxKm(int maxKm) {
        this.maxKm = maxKm;
    }

    public BigDecimal getListPrice() {
        return listPrice;
    }

    public void setListPrice(BigDecimal listPrice) {
        this.listPrice = listPrice;
    }

    public BigDecimal getAmountUpgrade() {
        return amountUpgrade;
    }

    public void setAmountUpgrade(BigDecimal amountUpgrade) {
        this.amountUpgrade = amountUpgrade;
    }

    public BigDecimal getAmountDowngrade() {
        return amountDowngrade;
    }

    public void setAmountDowngrade(BigDecimal amountDowngrade) {
        this.amountDowngrade = amountDowngrade;
    }

    public int getCo2() {
        return co2;
    }

    public void setCo2(int co2) {
        this.co2 = co2;
    }

    public int getFiscHp() {
        return fiscHp;
    }

    public void setFiscHp(int fiscHp) {
        this.fiscHp = fiscHp;
    }

    public Duration getTimeOfDeliveryInDays() {
        return timeOfDeliveryInDays;
    }

    public void setTimeOfDeliveryInDays(Duration timeOfDeliveryInDays) {
        this.timeOfDeliveryInDays = timeOfDeliveryInDays;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Car car = (Car) o;

        if (functionalLevel != car.functionalLevel) return false;
        if (towingBracketPossibility != car.towingBracketPossibility) return false;
        if (gps != car.gps) return false;
        if (bluetooth != car.bluetooth) return false;
        if (idealKm != car.idealKm) return false;
        if (maxKm != car.maxKm) return false;

        if (co2 != car.co2) return false;
        if (fiscHp != car.fiscHp) return false;
        if (make != null ? !make.equals(car.make) : car.make != null) return false;
        if (model != null ? !model.equals(car.model) : car.model != null) return false;
        if (description != null ? !description.equals(car.description) : car.description != null) return false;
//        if (packs != null ? !packs.equals(car.packs) : car.packs != null) return false;
        if (tyreType != car.tyreType) return false;
        if (fuelType != car.fuelType) return false;
        if (listPrice != null ? !listPrice.equals(car.listPrice) : car.listPrice != null) return false;
        if (amountUpgrade != null ? !amountUpgrade.equals(car.amountUpgrade) : car.amountUpgrade != null) return false;
        if (amountDowngrade != null ? !amountDowngrade.equals(car.amountDowngrade) : car.amountDowngrade != null)
            return false;
        return !(timeOfDeliveryInDays != null ? !timeOfDeliveryInDays.equals(car.timeOfDeliveryInDays) : car.timeOfDeliveryInDays != null);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = make != null ? make.hashCode() : 0;
        result = 31 * result + (model != null ? model.hashCode() : 0);
        result = 31 * result + functionalLevel;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (packs != null ? packs.hashCode() : 0);
        result = 31 * result + (tyreType != null ? tyreType.hashCode() : 0);
        result = 31 * result + (fuelType != null ? fuelType.hashCode() : 0);
        result = 31 * result + (towingBracketPossibility ? 1 : 0);
        result = 31 * result + (gps ? 1 : 0);
        result = 31 * result + (bluetooth ? 1 : 0);
        result = 31 * result + idealKm;
        result = 31 * result + maxKm;
        result = 31 * result + (listPrice != null ? listPrice.hashCode() : 0);
        result = 31 * result + (amountUpgrade != null ? amountUpgrade.hashCode() : 0);
        result = 31 * result + (amountDowngrade != null ? amountDowngrade.hashCode() : 0);
        result = 31 * result + co2;
        result = 31 * result + fiscHp;
        result = 31 * result + (timeOfDeliveryInDays != null ? timeOfDeliveryInDays.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Car{" +
                "make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", functionalLevel=" + functionalLevel +
                ", description='" + description + '\'' +
                ", packs=" + packs +
                ", tyreType=" + tyreType +
                ", fuelType=" + fuelType +
                ", towingBracketPossibility=" + towingBracketPossibility +
                ", gps=" + gps +
                ", bluetooth=" + bluetooth +
                ", idealKm=" + idealKm +
                ", maxKm=" + maxKm +
                ", listPrice=" + listPrice +
                ", benefit=" + benefit +
                ", amountUpgrade=" + amountUpgrade +
                ", amountDowngrade=" + amountDowngrade +
                ", co2=" + co2 +
                ", fiscHp=" + fiscHp +
                ", timeOfDeliveryInDays=" + timeOfDeliveryInDays +
                '}';
    }
}
