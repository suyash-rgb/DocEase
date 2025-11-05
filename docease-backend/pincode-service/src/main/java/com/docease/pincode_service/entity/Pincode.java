package com.docease.pincode_service.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "pincode")
public class Pincode {

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "officeName", column = @Column(name = "OfficeName")),
            @AttributeOverride(name = "pincode", column = @Column(name = "Pincode")),
            @AttributeOverride(name = "district", column = @Column(name = "District")),
            @AttributeOverride(name = "divisionName", column = @Column(name = "DivisionName"))
    })
    private PincodePrimaryKey pincodePrimaryKey;

    @Column(name = "CircleName", length = 100)
    private String circleName;

    @Column(name = "RegionName", nullable = false, length = 100)
    private String regionName;

    @Column(name = "OfficeType", length = 2)
    private String officeType;

    @Column(name = "Delivery")
    private Integer delivery;

    @Column(name = "StateName", length = 100)
    private String stateName;

    public Pincode() {}

    public Pincode(PincodePrimaryKey pincodePrimaryKey, String circleName, String regionName,
                   String officeType, Integer delivery, String stateName) {
        this.pincodePrimaryKey = pincodePrimaryKey;
        this.circleName = circleName;
        this.regionName = regionName;
        this.officeType = officeType;
        this.delivery = delivery;
        this.stateName = stateName;
    }

    // === Getters & Setters ===
    public PincodePrimaryKey getPincodePrimaryKey() {
        return pincodePrimaryKey;
    }

    public void setPincodePrimaryKey(PincodePrimaryKey pincodePrimaryKey) {
        this.pincodePrimaryKey = pincodePrimaryKey;
    }

    public String getCircleName() {
        return circleName;
    }

    public void setCircleName(String circleName) {
        this.circleName = circleName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getOfficeType() {
        return officeType;
    }

    public void setOfficeType(String officeType) {
        this.officeType = officeType;
    }

    public Integer getDelivery() {
        return delivery;
    }

    public void setDelivery(Integer delivery) {
        this.delivery = delivery;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    // === Convenience Getters from Embedded ID ===
    public String getOfficeName() {
        return pincodePrimaryKey != null ? pincodePrimaryKey.getOfficeName() : null;
    }

    public int getPincode() {
        return pincodePrimaryKey != null ? pincodePrimaryKey.getPincode() : 0;
    }

    public String getDistrict() {
        return pincodePrimaryKey != null ? pincodePrimaryKey.getDistrict() : null;
    }

    public String getDivisionName() {
        return pincodePrimaryKey != null ? pincodePrimaryKey.getDivisionName() : null;
    }

    // === equals(), hashCode(), toString() ===
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pincode pincode)) return false;
        return Objects.equals(pincodePrimaryKey, pincode.pincodePrimaryKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pincodePrimaryKey);
    }

    @Override
    public String toString() {
        return "Pincode{" +
                "pincodePrimaryKey=" + pincodePrimaryKey +
                ", circleName='" + circleName + '\'' +
                ", regionName='" + regionName + '\'' +
                ", officeType='" + officeType + '\'' +
                ", delivery=" + delivery +
                ", stateName='" + stateName + '\'' +
                '}';
    }
}
