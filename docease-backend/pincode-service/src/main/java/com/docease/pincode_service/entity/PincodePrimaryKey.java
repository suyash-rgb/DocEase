package com.docease.pincode_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PincodePrimaryKey implements Serializable {

    @Column(name = "OfficeName", nullable = false, length = 100)
    private String officeName;

    @Column(name = "Pincode", nullable = false)
    private int pincode;

    @Column(name = "District", nullable = false, length = 255)
    private String district;

    @Column(name = "DivisionName", nullable = false, length = 100)
    private String divisionName;

    // === Constructors ===
    public PincodePrimaryKey() {}

    public PincodePrimaryKey(String officeName, int pincode, String district, String divisionName) {
        this.officeName = officeName;
        this.pincode = pincode;
        this.district = district;
        this.divisionName = divisionName;
    }

    // === Getters & Setters ===
    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public int getPincode() {
        return pincode;
    }

    public void setPincode(int pincode) {
        this.pincode = pincode;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    // === equals(), hashCode(), toString() ===
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PincodePrimaryKey that)) return false;
        return pincode == that.pincode &&
                Objects.equals(officeName, that.officeName) &&
                Objects.equals(district, that.district) &&
                Objects.equals(divisionName, that.divisionName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(officeName, pincode, district, divisionName);
    }

    @Override
    public String toString() {
        return "PincodePrimaryKey{" +
                "officeName='" + officeName + '\'' +
                ", pincode=" + pincode +
                ", district='" + district + '\'' +
                ", divisionName='" + divisionName + '\'' +
                '}';
    }
}
