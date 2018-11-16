package com.shalimov.movieland.dao.jdbc;

import com.shalimov.movieland.dao.CountryDao;
import com.shalimov.movieland.dao.jdbc.mapper.CountryRowMapper;
import com.shalimov.movieland.entity.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcCountryDao implements CountryDao {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final RowMapper<Country> COUNTRY_ROW_MAPPER = new CountryRowMapper();
    private final String getAllCountriesSql = "SELECT c.id, c.name FROM country AS c;";
    private final String getCountryForMovie = "SELECT c.id, c.name FROM country AS c" +
            " LEFT JOIN movie_country AS mc ON mc.country = c.id" +
            " WHERE mc.movie=:movieId;";
    private final String ADD_COUNTRY_FOR_MOVIE_SQL = "INSERT INTO movie_country (movie,country) VALUES(:movie,:country)";
    private final String DELETE_COUNTRY_FOR_MOVIE = "DELETE FROM movie_country WHERE movie =:movieId;";
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public JdbcCountryDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Country> getAll() {
        logger.info("start receiving all countries");
        return namedParameterJdbcTemplate.query(getAllCountriesSql, COUNTRY_ROW_MAPPER);
    }

    @Override
    public List<Country> getCountryForMovie(int id) {
        logger.info("start receiving genres for movie with id {}", id);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("movieId", id);
        return namedParameterJdbcTemplate.query(getCountryForMovie, params, COUNTRY_ROW_MAPPER);
    }

    @Override
    public boolean addCountryForMovie(int countryId, int movieId) {
        logger.info("Start update movie_country");
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("movie", movieId);
        params.addValue("country", countryId);
        int result = namedParameterJdbcTemplate.update(ADD_COUNTRY_FOR_MOVIE_SQL, params);
        logger.info("Movie  saved");
        return result == 1;
    }

    @Override
    public void removeAllCountriesForMovie(int movieId) {
        namedParameterJdbcTemplate.update(DELETE_COUNTRY_FOR_MOVIE, new MapSqlParameterSource("movie", movieId));
    }
}
