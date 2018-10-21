package com.shalimov.movieland.service.impl;

import com.shalimov.movieland.dao.CountryDao;
import com.shalimov.movieland.entity.Country;
import com.shalimov.movieland.service.CountryService;

import java.util.List;

public class DefaultCountryService implements CountryService {
    private  CountryDao countryDao;

    @Override
    public List<Country> getAll() {
        return countryDao.getAll();
    }

    public void setCountryDao(CountryDao countryDao) {
        this.countryDao = countryDao;
    }
}
