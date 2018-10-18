package com.shalimov.movieland.dao.jdbc;

import com.shalimov.movieland.dao.MovieDao;
import com.shalimov.movieland.dao.jdbc.mapper.MovieExtractor;
import com.shalimov.movieland.entity.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

public class JdbcMovieDao implements MovieDao {

    private String getMoviesSql;
    private String getMovieByIdSql;
    private String getRandomMoviesSql;
    private String getMoviesByGenre;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final ResultSetExtractor<List<Movie>> MOVIE_EXTRACTOR = new MovieExtractor();
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<Movie> getAll() {
        logger.info("start receiving all movies");
        return namedParameterJdbcTemplate.query(getMoviesSql, MOVIE_EXTRACTOR);
    }

    @Override
    public List<Movie> getRandomMovies() {
        logger.info("start receiving random movies");
        return namedParameterJdbcTemplate.query(getMoviesSql + getRandomMoviesSql, MOVIE_EXTRACTOR);
    }

    @Override
    public List<Movie> getMoviesByGenre(int genreId) {
        logger.info("start receiving movies by genre with id {}", genreId);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("genreId", genreId);
        return namedParameterJdbcTemplate.query(getMoviesSql + getMoviesByGenre, params, MOVIE_EXTRACTOR);
    }

    @Override
    public Movie getMovieById(int movieId) {
        logger.info("start receiving movies  with id {}", movieId);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("movieId", movieId);
        return namedParameterJdbcTemplate.query(getMoviesSql+getMovieByIdSql,params,MOVIE_EXTRACTOR).get(0);
    }

    public void setGetMoviesSql(String getAllMoviesSql) {
        this.getMoviesSql = getAllMoviesSql;
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

    public void setGetMovieByIdSql(String getMovieByIdSql) {
        this.getMovieByIdSql = getMovieByIdSql;
    }
}
