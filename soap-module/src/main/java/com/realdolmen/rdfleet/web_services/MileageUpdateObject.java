//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.06 at 11:18:20 AM CET 
//


package com.realdolmen.rdfleet.web_services;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MileageUpdateObject complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MileageUpdateObject">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="LicensePlate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MileageDriven" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MileageUpdateObject", propOrder = {
    "licensePlate",
    "mileageDriven"
})
public class MileageUpdateObject {

    @XmlElement(name = "LicensePlate", required = true)
    protected String licensePlate;
    @XmlElement(name = "MileageDriven")
    protected int mileageDriven;

    /**
     * Gets the value of the licensePlate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLicensePlate() {
        return licensePlate;
    }

    /**
     * Sets the value of the licensePlate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLicensePlate(String value) {
        this.licensePlate = value;
    }

    /**
     * Gets the value of the mileageDriven property.
     * 
     */
    public int getMileageDriven() {
        return mileageDriven;
    }

    /**
     * Sets the value of the mileageDriven property.
     * 
     */
    public void setMileageDriven(int value) {
        this.mileageDriven = value;
    }

}
