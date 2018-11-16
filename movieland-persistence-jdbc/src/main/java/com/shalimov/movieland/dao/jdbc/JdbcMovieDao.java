package com.shalimov.movieland.dao.jdbc;

import com.shalimov.movieland.dao.MovieDao;
import com.shalimov.movieland.dao.jdbc.mapper.MovieRowMapper;
import com.shalimov.movieland.entity.Movie;
import com.shalimov.movieland.entity.MovieRequest;
import com.shalimov.movieland.entity.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcMovieDao implements MovieDao {

    private final String getMoviesSql = "SELECT m.id, m.name_russian, m.name_native, m.year_of_release, m.description, m.rating, m.price, " +
            "m.picture_path FROM movie AS m";
    private final String ADD_NEW_MOVIE_SQL = "INSERT INTO movie (name_russian,name_native,year_of_release,description,rating" +
            ",price,picture_path) VALUES(:name_russian, :name_native, :year_of_release,:description,:rating,:price,:picture_path)";
    private final String EDIT_MOVIE_SQL = "UPDATE movie SET name_russian=:name_russian,name_native=:name_native," +
            "year_of_release=year_of_release,description=:description,rating=:rating,price=:price,picture_path=:picture_path WHERE id=:id";
    private final String getMovieByIdSql = " WHERE m.id=:movieId;";
    private final String getRandomMoviesSql = " order by random() limit 3;";
    private final String getMoviesByGenre = " WHERE g.id=:genreId";
    private final String DELETE_MOVIE_SQL = "DELETE FROM movie WHERE id =:id;";
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final RowMapper<Movie> MOVIE_ROW_MAPPER = new MovieRowMapper();
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public JdbcMovieDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Movie> getAll(MovieRequest movieRequest) {
        logger.info("start receiving all movies");

        return namedParameterJdbcTemplate.query(getMoviesSql + addSortingSql(movieRequest), MOVIE_ROW_MAPPER);
    }

    @Override
    public List<Movie> getRandomMovies() {
        logger.info("start receiving random movies");
        return namedParameterJdbcTemplate.query(getMoviesSql + getRandomMoviesSql, MOVIE_ROW_MAPPER);
    }

    @Override
    public List<Movie> getMoviesByGenre(int genreId, MovieRequest movieRequest) {
        logger.info("start receiving movies by genre with id {}", genreId);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("genreId", genreId);
        return namedParameterJdbcTemplate.query(getMoviesSql + getMoviesByGenre + addSortingSql(movieRequest), params, MOVIE_ROW_MAPPER);
    }

    @Override
    public Movie getMovieById(int movieId) {
        logger.info("start receiving movies  with id {}", movieId);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("movieId", movieId);
        return namedParameterJdbcTemplate.query(getMoviesSql + getMovieByIdSql, params, MOVIE_ROW_MAPPER).get(0);
    }

    @Override
    public boolean addMovie(Movie movie) {
        logger.info("Start update movie");
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name_russian", movie.getNameRussian());
        params.addValue("name_native", movie.getNameNative());
        params.addValue("year_of_release", movie.getYearOfRelease());
        params.addValue("description", movie.getDescription());
        params.addValue("rating", movie.getRating());
        params.addValue("price", movie.getPrice());
        params.addValue("picture_path", movie.getPicturePath());
        int result = namedParameterJdbcTemplate.update(ADD_NEW_MOVIE_SQL, params);
        logger.info("Movie  saved");
        return result == 1;
    }

    @Override
    public void deleteMovie(Integer movieId) {
        namedParameterJdbcTemplate.update(DELETE_MOVIE_SQL, new MapSqlParameterSource("id", movieId));
    }

    @Override
    public boolean editMovie(Movie movie) {
        logger.info("Start upload movie");
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", movie.getId());
        params.addValue("name_russian", movie.getNameRussian());
        params.addValue("name_native", movie.getNameNative());
        params.addValue("year_of_release", movie.getYearOfRelease());
        params.addValue("description", movie.getDescription());
        params.addValue("rating", movie.getRating());
        params.addValue("price", movie.getPrice());
        params.addValue("picture_path", movie.getPicturePath());
        int result = namedParameterJdbcTemplate.update(EDIT_MOVIE_SQL, params);
        logger.info("Movie  update");
        return result == 1;
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
