package com.shalimov.movieland.service.impl;

import com.shalimov.movieland.dao.CountryDao;
import com.shalimov.movieland.entity.Country;
import com.shalimov.movieland.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultCountryService implements CountryService {
    private final CountryDao cachedCountryDao;

    @Autowired
    public DefaultCountryService(CountryDao cachedCountryDao) {
        this.cachedCountryDao = cachedCountryDao;
    }

    @Override
    public List<Country> getAll() {
        return cachedCountryDao.getAll();
    }


}
