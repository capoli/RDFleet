package com.realdolmen.rdfleet.domain;

import com.realdolmen.rdfleet.converters.DurationPersistenceConverter;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Duration;
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
    @ManyToMany
    private List<Pack> packs;
    @Enumerated(EnumType.STRING)
    @NotNull
    private TyreType tyreType;
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
    private double benefit;
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

    public double getBenefit() {
        return benefit;
    }

    public void setBenefit(double benefit) {
        this.benefit = benefit;
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
}
