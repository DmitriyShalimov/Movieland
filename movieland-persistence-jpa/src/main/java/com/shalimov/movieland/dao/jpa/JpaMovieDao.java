package com.shalimov.movieland.dao.jpa;

import com.shalimov.movieland.dao.MovieDao;
import com.shalimov.movieland.dao.jpa.dto.CountryDto;
import com.shalimov.movieland.dao.jpa.dto.GenreDto;
import com.shalimov.movieland.dao.jpa.dto.MovieDto;
import com.shalimov.movieland.entity.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
@Transactional
public class JpaMovieDao implements MovieDao {


    private final SessionFactory sessionFactory;

    @Value("${movies.per.page}")
    private int moviesPerPage;

    @Autowired
    public JpaMovieDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override

    public List<Movie> getAll(MovieRequest movieRequest) {
        Query<MovieDto> movieDtoQuery = getCurrentSession().createQuery("select m from MovieDto m", MovieDto.class);
        List<MovieDto> moviesDto = movieDtoQuery.setMaxResults(moviesPerPage).getResultList();
        return movieDtoConverter(moviesDto);
    }

    @Override
    public List<Movie> getRandomMovies() {
        Query<MovieDto> movieDtoQuery = getCurrentSession().createQuery("select m from MovieDto m  order by random()", MovieDto.class);
        List<MovieDto> moviesDto = movieDtoQuery.setMaxResults(3).getResultList();
        return movieDtoConverter(moviesDto);
    }

    @Override
    public List<Movie> getMoviesByGenre(int genreId, MovieRequest movieRequest) {
        Query<MovieDto> movieDtoQuery = getCurrentSession().createQuery("select m from MovieDto m, GenreDto g  where g.id=:genreId", MovieDto.class);
        movieDtoQuery.setParameter("genreId", genreId);
        List<MovieDto> moviesDto = movieDtoQuery.setMaxResults(moviesPerPage).getResultList();
        return movieDtoConverter(moviesDto);
    }

    @Override
    public Movie getMovieById(int movieId) {
        MovieDto movieDto = getCurrentSession().find(MovieDto.class, movieId);
        return movieDtoConverter(Collections.singletonList(movieDto)).get(0);
    }

    @Override
    public void editMovie(Movie movie) {
        MovieDto movieDto = movieConverter(movie);
        getCurrentSession().merge(movieDto);
    }

    @Override
    public int addMovie(Movie movie) {
        MovieDto movieDto = movieConverter(movie);
        getCurrentSession().merge(movieDto);
        getCurrentSession().flush();
        return movieDto.getId();
    }

    @Override
    public void deleteMovie(List<Integer> movieId) {
        for (int id : movieId) {
            Movie entity = getMovieById(id);
            getCurrentSession().remove(entity);
        }
    }

    @Override
    public List<Movie> getMoviesByMask(String mask, MovieRequest movieRequest) {
        TypedQuery<MovieDto> movieDtoQuery = getCurrentSession().createQuery("select m from MovieDto m " +
                " WHERE LOWER (m.nameRussian) LIKE LOWER(:mask) OR LOWER(m.nameNative) LIKE LOWER(:mask)", MovieDto.class);
        movieDtoQuery.setParameter("mask", "%" + mask + "%");
        List<MovieDto> moviesDto = movieDtoQuery.setMaxResults(moviesPerPage).getResultList();
        return movieDtoConverter(moviesDto);
    }

    @Override
    public List<Movie> getMoviesForReport(ReportRequest reportRequest) {
        if (reportRequest.getType() == ReportRequestType.ALL_MOVIES) {
            Query<MovieDto> movieDtoQuery = getCurrentSession().createQuery("select m from MovieDto m", MovieDto.class);
            List<MovieDto> moviesDto = movieDtoQuery.getResultList();
            return movieDtoConverter(moviesDto);

        } else {
            String period = " where m.addDate >" + reportRequest.getStartDate() + " and m.addDate< " + reportRequest.getEndDate();
            TypedQuery<MovieDto> movieDtoQuery = getCurrentSession().createQuery("select m from MovieDto m" + period, MovieDto.class);
            List<MovieDto> moviesDto = movieDtoQuery.setMaxResults(moviesPerPage).getResultList();
            return movieDtoConverter(moviesDto);
        }
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    private List<Movie> movieDtoConverter(List<MovieDto> moviesDto) {
        List<Movie> movies = new ArrayList<>();
        for (MovieDto movieDto : moviesDto) {
            Movie movie = new Movie();
            movie.setId(movieDto.getId());
            movie.setNameRussian(movieDto.getNameRussian());
            movie.setNameNative(movieDto.getNameNative());
            movie.setDescription(movieDto.getDescription());
            movie.setPicturePath(movieDto.getPicturePath());
            movie.setPrice(movieDto.getPrice());
            movie.setYearOfRelease(movieDto.getYearOfRelease());
            movie.setRating(movieDto.getRating());
            movie.setCountries(countryDtoConverter(movieDto.getCountries()));
            movie.setGenres(genreDtoConverter(movieDto.getGenres()));
            movies.add(movie);
        }
        return movies;
    }

    private MovieDto movieConverter(Movie movie) {
        MovieDto movieDto = new MovieDto();
        movieDto.setId(movie.getId());
        movieDto.setNameRussian(movie.getNameRussian());
        movieDto.setNameNative(movie.getNameNative());
        movieDto.setDescription(movie.getDescription());
        movieDto.setPicturePath(movie.getPicturePath());
        movieDto.setPrice(movie.getPrice());
        movieDto.setYearOfRelease(movie.getYearOfRelease());
        movieDto.setRating(movie.getRating());
        List<CountryDto> countryDtos = new ArrayList<>();
        for (Country country : movie.getCountries()) {
            CountryDto countryDto = new CountryDto();
            countryDto.setId(country.getId());
            countryDto.setTitle(country.getName());
            countryDtos.add(countryDto);
        }
        movieDto.setCountries(countryDtos);
        List<GenreDto> genreDtos = new ArrayList<>();
        for (Genre genre : movie.getGenres()) {
            GenreDto genreDto = new GenreDto();
            genreDto.setId(genre.getId());
            genreDto.setTitle(genre.getTitle());
            genreDtos.add(genreDto);
        }
        movieDto.setGenres(genreDtos);

        return movieDto;
    }

    private List<Genre> genreDtoConverter(List<GenreDto> genresDto) {
        List<Genre> genres = new ArrayList<>();
        for (GenreDto genreDto : genresDto) {
            Genre genre = new Genre(genreDto.getId(), genreDto.getTitle());
            genres.add(genre);
        }
        return genres;
    }

    private List<Country> countryDtoConverter(List<CountryDto> countriesDto) {
        List<Country> countries = new ArrayList<>();
        for (CountryDto countryDto : countriesDto) {
            Country country = new Country(countryDto.getId(), countryDto.getTitle());
            countries.add(country);
        }
        return countries;
    }
}
