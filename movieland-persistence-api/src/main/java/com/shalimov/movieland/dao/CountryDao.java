package com.shalimov.movieland.dao;


import com.shalimov.movieland.entity.Country;

import java.util.List;

public interface CountryDao {

    List<Country> getAll();

    List<Country> getCountryForMovie(int id);

    boolean addCountryForMovie(int countryId, int movieId);

    void removeAllCountriesForMovie(int movieId);
}
