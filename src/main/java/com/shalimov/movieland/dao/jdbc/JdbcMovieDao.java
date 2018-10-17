package com.shalimov.movieland.dao.jdbc;

import com.shalimov.movieland.dao.MovieDao;
import com.shalimov.movieland.dao.jdbc.mapper.MovieRowMapper;
import com.shalimov.movieland.entity.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

public class JdbcMovieDao implements MovieDao {

    private String getAllMoviesSql;
    private String getRandomMoviesSql;
    private String getMoviesByGenre;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final RowMapper<Movie> MOVIE_ROW_MAPPER = new MovieRowMapper();
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<Movie> getAll() {
        logger.info("start receiving all movies");
        return namedParameterJdbcTemplate.query(getAllMoviesSql, MOVIE_ROW_MAPPER);
    }

    @Override
    public List<Movie> getRandomMovies() {
        logger.info("start receiving random movies");
        return namedParameterJdbcTemplate.query(getRandomMoviesSql, MOVIE_ROW_MAPPER);
    }

    @Override
    public List<Movie> getMoviesByGenre(int id) {
        logger.info("start receiving movies by genre with id {}", id);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("genreId", id);
        return namedParameterJdbcTemplate.query(getMoviesByGenre, params, MOVIE_ROW_MAPPER);
    }

    public void setGetAllMoviesSql(String getAllMoviesSql) {
        this.getAllMoviesSql = getAllMoviesSql;
    }

    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public void setGetRandomMoviesSql(String getRandomMoviesSql) {
        this.getRandomMoviesSql = getRandomMoviesSql;
    }

    public void setGetMoviesByGenre(String getMoviesByGenre) {
        this.getMoviesByGenre = getMoviesByGenre;
    }
}
