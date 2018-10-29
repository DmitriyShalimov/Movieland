package com.shalimov.movieland.dao.jdbc;

import com.shalimov.movieland.dao.MovieDao;
import com.shalimov.movieland.dao.jdbc.mapper.MovieExtractor;
import com.shalimov.movieland.entity.Movie;
import com.shalimov.movieland.entity.MovieRequest;
import com.shalimov.movieland.entity.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcMovieDao implements MovieDao {

    private final String getMoviesSql = "SELECT m.id, m.name_russian, m.name_native, m.year_of_release, m.description, " +
            "m.rating, m.price,m.picture_path,c.id AS country_id,c.name AS country_name,g.id AS genre_id,g.name AS genre_name FROM movie AS m" +
            " LEFT JOIN movie_country mc ON mc.movie = m.id" +
            " LEFT JOIN country c ON c.id = mc.country" +
            " LEFT JOIN movie_genre mg ON mg.movie = m.id" +
            " LEFT JOIN genre g ON g.id = mg.genre";
    private final String getMovieByIdSql = " WHERE m.id=:movieId;";
    private final String getRandomMoviesSql = " order by random() limit 3;";
    private final String getMoviesByGenre = " WHERE g.id=:genreId";
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final ResultSetExtractor<List<Movie>> MOVIE_EXTRACTOR = new MovieExtractor();
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public JdbcMovieDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Movie> getAll(MovieRequest movieRequest) {
        logger.info("start receiving all movies");

        return namedParameterJdbcTemplate.query(getMoviesSql + addSortingSql(movieRequest), MOVIE_EXTRACTOR);
    }

    @Override
    public List<Movie> getRandomMovies() {
        logger.info("start receiving random movies");
        return namedParameterJdbcTemplate.query(getMoviesSql + getRandomMoviesSql, MOVIE_EXTRACTOR);
    }

    @Override
    public List<Movie> getMoviesByGenre(int genreId, MovieRequest movieRequest) {
        logger.info("start receiving movies by genre with id {}", genreId);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("genreId", genreId);
        return namedParameterJdbcTemplate.query(getMoviesSql + getMoviesByGenre + addSortingSql(movieRequest), params, MOVIE_EXTRACTOR);
    }

    @Override
    public Movie getMovieById(int movieId) {
        logger.info("start receiving movies  with id {}", movieId);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("movieId", movieId);
        return namedParameterJdbcTemplate.query(getMoviesSql + getMovieByIdSql, params, MOVIE_EXTRACTOR).get(0);
    }

    private String addSortingSql(MovieRequest movieRequest) {
        if (SortType.DESC.equals(movieRequest.getRatingOrder())) {
            return " ORDER BY m.rating DESC";
        }
        if (SortType.DESC.equals(movieRequest.getPriceOrder())) {
            return " ORDER BY m.price DESC";
        }
        if (SortType.ASC.equals(movieRequest.getPriceOrder())) {
            return " ORDER BY m.price";
        }
        return "";
    }
}
