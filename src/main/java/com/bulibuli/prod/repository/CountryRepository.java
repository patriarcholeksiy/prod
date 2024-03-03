package com.bulibuli.prod.repository;

import com.bulibuli.prod.entity.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CountryRepository extends JpaRepository<CountryEntity, Long> {

    CountryEntity getByAlpha2(String alpha2);

    List<CountryEntity> getAllByRegionIsIn(List<String> regions);

    boolean existsByRegion(String region);

    boolean existsByAlpha2(String alpha2);

    boolean existsAllByRegionIsIn(List<String> regions);
}
