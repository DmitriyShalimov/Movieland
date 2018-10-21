package com.shalimov.movieland.dao.cache;

import com.shalimov.movieland.dao.CountryDao;
import com.shalimov.movieland.dao.GenreDao;
import com.shalimov.movieland.entity.Country;
import com.shalimov.movieland.entity.Genre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;

public class CachedCountryDao implements CountryDao {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private CountryDao countryDao;
    private List<Country> allCountries;

    @Override
    public List<Country> getAll() {
        return new ArrayList<>(allCountries);
    }

    @Scheduled(fixedDelay = 4 * 60 * 60 * 1000, initialDelay = 0)
    private void invalidate() {
        logger.info("Update country cache");
        allCountries = countryDao.getAll();
    }

    public void setCountryDao(CountryDao countryDao) {
        this.countryDao = countryDao;
    }
}
