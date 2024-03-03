package com.bulibuli.prod.service;

import com.bulibuli.prod.entity.CountryEntity;
import com.bulibuli.prod.repository.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService {

    private CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public CountryEntity getByAlpha2(String alpha2) {
        return countryRepository.getByAlpha2(alpha2);
    }

    public List<CountryEntity> getAll() {
        return countryRepository.findAll();
    }

    public List<CountryEntity> getAllByRegion(List<String> regions) {
        return countryRepository.getAllByRegionIsIn(regions);
    }

    public boolean existsByRegion(String region) {
        return countryRepository.existsByRegion(region);
    }

    public boolean existsByAlpha2(String alpha2) {
        return countryRepository.existsByAlpha2(alpha2);
    }

    public boolean existsAllByRegions(List<String> regions) {
        for (String r: regions) {
            if (!existsByRegion(r)) {
                return false;
            }
        }
        return true;
    }

}
