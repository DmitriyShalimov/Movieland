package com.shalimov.movieland.dao.jdbc;

import com.shalimov.movieland.dao.MovieDao;
import com.shalimov.movieland.dao.jdbc.mapper.MovieRowMapper;
import com.shalimov.movieland.dao.jdbc.mapper.MovieWitDateRowMapper;
import com.shalimov.movieland.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcMovieDao implements MovieDao {

    private final String getMoviesSql = "SELECT m.id, m.name_russian, m.name_native, m.year_of_release, m.description, m.rating, m.price, " +
            "m.picture_path FROM movie AS m";
    private final String addNewMovieSql = "INSERT INTO movie (name_russian,name_native,year_of_release,description,rating" +
            ",price,picture_path) VALUES(:name_russian, :name_native, :year_of_release,:description,:rating,:price,:picture_path) RETURNING id";
    private final String editMovieSql = "UPDATE movie SET name_russian=:name_russian,name_native=:name_native," +
            "year_of_release=year_of_release,description=:description,rating=:rating,price=:price,picture_path=:picture_path WHERE id=:id";
    private final String getMovieByIdSql = " WHERE m.id=:movieId;";
    private final String getRandomMoviesSql = " order by random() limit 3;";
    private final String getMoviesByGenre = " WHERE g.id=:genreId";
    private final String deleteMovieSql = "DELETE FROM movie WHERE id =:id;";
    private final String getByMask = " WHERE LOWER (m.name_native) LIKE LOWER(:mask) OR LOWER(m.name_russian) LIKE LOWER(:mask)";
    private final String limit = " LIMIT :moviesPerPage OFFSET :offset";
    private final String getMoviesWithDateSql = "SELECT m.id,m.name_russian, m.name_native, m.year_of_release, m.description, m.rating, m.price, " +
            "m.picture_path, m.add_date, m.modify_date, m.rewiew_count FROM movie AS m";

    private @Value("${movies.per.page}")
    int moviesPerPage;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final RowMapper<Movie> MOVIE_ROW_MAPPER = new MovieRowMapper();
    private static final RowMapper<Movie> MOVIE_WITH_DARE_ROW_MAPPER = new MovieWitDateRowMapper();
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public JdbcMovieDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Movie> getAll(MovieRequest movieRequest) {
        logger.info("start receiving all movies");
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("offset", (movieRequest.getPage() - 1) * moviesPerPage);
        params.addValue("moviesPerPage", moviesPerPage);
        return namedParameterJdbcTemplate.query(getMoviesSql + addSortingSql(movieRequest) + limit, params, MOVIE_ROW_MAPPER);
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
        params.addValue("moviesPerPage", moviesPerPage);
        return namedParameterJdbcTemplate.query(getMoviesSql + getMoviesByGenre + addSortingSql(movieRequest) + limit, params, MOVIE_ROW_MAPPER);
    }

    @Override
    public Movie getMovieById(int movieId) {
        logger.info("start receiving movies  with id {}", movieId);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("movieId", movieId);
        return namedParameterJdbcTemplate.query(getMoviesSql + getMovieByIdSql, params, MOVIE_ROW_MAPPER).get(0);
    }

    @Override
    public int addMovie(Movie movie) {
        logger.info("Start update movie");
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name_russian", movie.getNameRussian());
        params.addValue("name_native", movie.getNameNative());
        params.addValue("year_of_release", movie.getYearOfRelease());
        params.addValue("description", movie.getDescription());
        params.addValue("rating", movie.getRating());
        params.addValue("price", movie.getPrice());
        params.addValue("picture_path", movie.getPicturePath());
        int result = namedParameterJdbcTemplate.queryForObject(addNewMovieSql, params, Integer.class);
        logger.info("Movie  saved");
        return result;
    }

    @Override
    public void deleteMovie(List<Integer> movieId) {
        namedParameterJdbcTemplate.update(deleteMovieSql, new MapSqlParameterSource("id", movieId));
    }

    @Override
    public List<Movie> getMoviesByMask(String mask, MovieRequest movieRequest) {
        logger.info("start receiving movies by mask {}", mask);
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("offset", (movieRequest.getPage() - 1) * moviesPerPage);
        params.addValue("mask", "%" + mask + "%");
        params.addValue("moviesPerPage", moviesPerPage);
        return namedParameterJdbcTemplate.query(getMoviesSql + getByMask + limit, params, MOVIE_ROW_MAPPER);
    }

    @Override
    public List<Movie> getMoviesForReport(ReportRequest reportRequest) {

        if (reportRequest.getType() == ReportRequestType.ALL_MOVIES) {
            return namedParameterJdbcTemplate.query(getMoviesWithDateSql, MOVIE_WITH_DARE_ROW_MAPPER);
        } else {
            String period=" where m.add_date >"+reportRequest.getStartDate()+" and m.add_date< "+reportRequest.getEndDate();
            return namedParameterJdbcTemplate.query(getMoviesWithDateSql+period, MOVIE_WITH_DARE_ROW_MAPPER);
        }
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
        int result = namedParameterJdbcTemplate.update(editMovieSql, params);
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
