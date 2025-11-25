package com.docease.user_service.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "hospitals")
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hospital_id")
    private Integer hospitalId;

    private String name;
    private String city;
    private String address;
    private String type;

    @Column(name = "rating")
    private Double rating;

    public Hospital() {
    }

    public Hospital(Integer hospitalId, String name, String city, String address, String type, Double rating) {
        this.hospitalId = hospitalId;
        this.name = name;
        this.city = city;
        this.address = address;
        this.type = type;
        this.rating = rating;
    }

    public Integer getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Integer hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}