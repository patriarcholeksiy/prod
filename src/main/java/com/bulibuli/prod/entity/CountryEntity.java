package com.bulibuli.prod.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "countries")
public class CountryEntity implements Comparable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String alpha2;

    private String alpha3;

    private String region;

    @Override
    public int compareTo(Object o) {
        CountryEntity country = (CountryEntity) o;
        return this.getAlpha2().compareTo(country.alpha2);
    }
}
