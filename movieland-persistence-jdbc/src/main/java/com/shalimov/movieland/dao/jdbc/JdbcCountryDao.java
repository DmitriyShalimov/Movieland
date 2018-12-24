package com.shalimov.movieland.dao.jdbc;

import com.shalimov.movieland.dao.CountryDao;
import com.shalimov.movieland.dao.jdbc.mapper.CountryRowMapper;
import com.shalimov.movieland.entity.Country;
import com.shalimov.movieland.entity.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
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
    private final String DELETE_COUNTRY_FOR_MOVIE = "DELETE FROM movie_country WHERE movie IN(:movieIds)";
    private final String getCountriesForMoviesSql = "SELECT mc.country,mc.movie,c.name FROM movie_country AS mc " +
            "LEFT JOIN country c ON c.id = mc.country WHERE mc.movie IN(:movieIds)";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcCountryDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
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
    public void addCountriesForMovie(List<Country> countries, int movieId) {
        logger.info("Start update movie_country");
        jdbcTemplate.batchUpdate(ADD_COUNTRY_FOR_MOVIE_SQL, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Country country = countries.get(i);
                ps.setInt(1, movieId);
                ps.setInt(2, country.getId());
            }

            @Override
            public int getBatchSize() {
                return countries.size();
            }
        });
        logger.info("Movie_country is updated");
    }

    @Override
    public void removeAllCountriesForMovie(List<Integer> movies) {
        namedParameterJdbcTemplate.update(DELETE_COUNTRY_FOR_MOVIE, new MapSqlParameterSource("movieIds", movies));
    }

    @Override
    public void enrich(List<Movie> movies, List<Integer> movieIds) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("movieIds", movieIds);
        namedParameterJdbcTemplate.query(getCountriesForMoviesSql, params, (resultSet, i) -> {
            int movieId = resultSet.getInt("movie");
            int countryId = resultSet.getInt("country");
            String countryName = resultSet.getString("name");
            for (Movie movie : movies) {
                if (movie.getId() == movieId) {
                    Country country = new Country(countryId, countryName);
                    movie.getCountries().add(country);
                }
            }
            return 1;
        });
    }
}
