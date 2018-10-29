package com.shalimov.movieland.web.controller;

import com.shalimov.movieland.entity.Country;
import com.shalimov.movieland.service.CountryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/country")
public class CountryController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping(value = "")
    public List<Country> getAllCountries() {
        logger.info("Retrieving all countries");
        List<Country> countries = countryService.getAll();
        logger.info("Countries  are {}", countries);
        return countries;
    }
}
