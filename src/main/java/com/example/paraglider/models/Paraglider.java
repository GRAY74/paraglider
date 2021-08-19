package com.example.paraglider.models;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class Paraglider {

    @Id
    @GeneratedValue()
    private Long id;

    @ManyToOne
    Brand brand;

    @NotEmpty(message = "Имя не должно быть пустым")
    String name;

    int numberSections;
    float length;

    Sertificat sertificat;

    public enum Sertificat{
        ENA("EN-A"),
        ENB("EN-B"),
        HENB("High EN-B"),
        ENC("EN-C"),
        END("EN-D");

        String label;
        public String getLabel() {return label;}

        Sertificat(String label) {
            this.label = label;
        }
    }
//-----------------------------------------------------
    public Paraglider() {
    }

    public Paraglider(Brand brand, String name, Sertificat sertificat) {
        this.brand = brand;
        this.name = name;
        this.sertificat = sertificat;
    }

    //------------------------------------------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getNumberSections() {
        return numberSections;
    }

    public void setNumberSections(int numberSections) {
        this.numberSections = numberSections;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public Sertificat getSertificat() {
        return sertificat;
    }

    public void setSertificat(Sertificat sertificat) {
        this.sertificat = sertificat;
    }
}
