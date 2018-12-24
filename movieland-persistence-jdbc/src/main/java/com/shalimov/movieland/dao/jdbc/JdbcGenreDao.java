package com.shalimov.movieland.dao.jdbc;

import com.shalimov.movieland.dao.GenreDao;
import com.shalimov.movieland.dao.jdbc.mapper.GenreRowMapper;
import com.shalimov.movieland.entity.Genre;
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
public class JdbcGenreDao implements GenreDao {
    private static final RowMapper<Genre> GENRE_ROW_MAPPER = new GenreRowMapper();
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final String getAllGenresSql = "SELECT g.id, g.name FROM genre AS g;";
    private final String getGenreForMovie = "SELECT g.id, g.name FROM genre AS g" +
            " LEFT JOIN movie_genre AS mg ON mg.genre = g.id" +
            " WHERE mg.movie=:movieId";
    private final String ADD_GENRE_FOR_MOVIE_SQL = "INSERT INTO movie_genre (movie,genre) VALUES(:movie,:genre)";
    private final String DELETE_GENRE_FOR_MOVIE = "DELETE FROM movie_genre WHERE movie IN(:movieIds);";
    private final String getGenresForMoviesSql = "SELECT mg.genre,mg.movie,g.name FROM movie_genre AS mg " +
            "LEFT JOIN genre g ON g.id = mg.genre WHERE mg.movie IN(:movieIds)";
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcGenreDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getAll() {
        logger.info("start receiving all genres");
        return namedParameterJdbcTemplate.query(getAllGenresSql, GENRE_ROW_MAPPER);
    }

    @Override
    public List<Genre> getGenreForMovie(int id) {
        logger.info("start receiving genres for movie with id {}", id);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("movieId", id);
        return namedParameterJdbcTemplate.query(getGenreForMovie, params, GENRE_ROW_MAPPER);
    }

    @Override
    public void removeAllGenresForMovie(List<Integer> movieIds) {
        namedParameterJdbcTemplate.update(DELETE_GENRE_FOR_MOVIE, new MapSqlParameterSource("movieIds", movieIds));
    }

    @Override
    public void enrich(List<Movie> movies, List<Integer> movieIds) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("movieIds", movieIds);namedParameterJdbcTemplate.query(getGenresForMoviesSql, params, (resultSet, i) -> {
            int movieId = resultSet.getInt("movie");
            int genreId = resultSet.getInt("genre");
            String genreName = resultSet.getString("name");
            for (Movie movie : movies) {
                if (movie.getId() == movieId) {
                    Genre genre = new Genre(genreId, genreName);
                    movie.getGenres().add(genre);
                }
            }
            return 1;
        });
    }

    @Override
    public void addGenresForMovie(List<Genre> genres, int movieId) {
        logger.info("Start update movie_genre");
        jdbcTemplate.batchUpdate(ADD_GENRE_FOR_MOVIE_SQL, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Genre genre = genres.get(i);
                ps.setInt(1, movieId);
                ps.setInt(2, genre.getId());
            }

            @Override
            public int getBatchSize() {
                return genres.size();
            }
        });
        logger.info("Movie_genre is updated");
    }
}
