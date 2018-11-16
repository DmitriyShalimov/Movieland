package com.shalimov.movieland.dao.cache;

import com.shalimov.movieland.dao.CountryDao;
import com.shalimov.movieland.entity.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;


@Service
public class CachedCountryDao implements CountryDao {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final CountryDao jdbcCountryDao;
    private volatile List<Country> allCountries;


    @Autowired
    public CachedCountryDao(CountryDao jdbcCountryDao) {
        this.jdbcCountryDao = jdbcCountryDao;
    }

    @Override
    public List<Country> getAll() {
        return new ArrayList<>(allCountries);
    }

    @Override
    public List<Country> getCountryForMovie(int id) {
        return null;
    }

    @Override
    public boolean addCountryForMovie(int countryId, int id) {
        return false;
    }

    @Override
    public void removeAllCountriesForMovie(int id) {

    }

    @PostConstruct
    @Scheduled(fixedDelayString = "${cache.update.time}", initialDelayString = "${cache.update.time}")
    private void invalidate() {
        logger.info("Update country cache");
        allCountries = jdbcCountryDao.getAll();
    }


}
