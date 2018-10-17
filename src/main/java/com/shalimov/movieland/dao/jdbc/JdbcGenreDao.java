package com.shalimov.movieland.dao.jdbc;

import com.shalimov.movieland.dao.GenreDao;
import com.shalimov.movieland.dao.jdbc.mapper.GenreRowMapper;
import com.shalimov.movieland.entity.Genre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcGenreDao implements GenreDao {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private String getAllGenresSql;
    private String getGenreForMovie;
    private static final RowMapper<Genre> GENRE_ROW_MAPPER = new GenreRowMapper();

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<Genre> getGenreForMovie(int id) {
        logger.info("start receiving genres for movie with id {}", id);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("movieId", id);
        return namedParameterJdbcTemplate.query(getGenreForMovie, params, GENRE_ROW_MAPPER);
    }

    @Override
    public List<Genre> getAll() {
        logger.info("start receiving all genres");
        return namedParameterJdbcTemplate.query(getAllGenresSql, GENRE_ROW_MAPPER);
    }

    public void setGetAllGenresSql(String getAllGenresSql) {
        this.getAllGenresSql = getAllGenresSql;
    }

    public void setGetGenreForMovie(String getGenreForMovie) {
        this.getGenreForMovie = getGenreForMovie;
    }

    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }
}
