package com.example.paraglider.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

@Entity
public class Brand {

    @Id
    @GeneratedValue
    private Integer id;

    @NotEmpty(message = "Имя не должно быть пустым")
    String name;

    String country;

    String site;

//-------------------------------------------------
    public Brand() {
    }

    public Brand(String name, String country, String site) {
        this.name = name;
        this.country = country;
        this.site = site;
    }

    //-------------------------------------------------
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }
}
