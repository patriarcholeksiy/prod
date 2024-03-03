package com.bulibuli.prod.controller;

import com.bulibuli.prod.entity.CountryEntity;
import com.bulibuli.prod.service.CountryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/countries")
public class CountryController {

    private CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    public ResponseEntity<?> fetchCountries(@RequestParam(required = false) List<String> region) {
        List<CountryEntity> countries;
        if (region != null) {
            if (countryService.existsAllByRegions(region)){
                countries = countryService.getAllByRegion(region);
                Collections.sort(countries);
                return ResponseEntity.status(HttpStatus.OK).body(countries);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        countries = countryService.getAll();
        Collections.sort(countries);
        return ResponseEntity.status(HttpStatus.OK).body(countries);
    }

    @GetMapping("/{alpha2}")
    public ResponseEntity<?> fetchCountryByAlpha2(@PathVariable String alpha2) {
        if (countryService.existsByAlpha2(alpha2))
            return ResponseEntity.status(HttpStatus.OK).body(countryService.getByAlpha2(alpha2));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Страна с указанным кодом не найдена.");
    }

}
