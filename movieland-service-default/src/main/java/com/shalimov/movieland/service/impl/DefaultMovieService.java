package com.shalimov.movieland.service.impl;

import com.shalimov.movieland.dao.MovieDao;
import com.shalimov.movieland.entity.*;
import com.shalimov.movieland.service.CountryService;
import com.shalimov.movieland.service.GenreService;
import com.shalimov.movieland.service.cache.CurrencyService;
import com.shalimov.movieland.service.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Service
public class DefaultMovieService implements MovieService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final MovieDao movieDao;
    private final CurrencyService currencyService;
    private final CountryService countryService;
    private final GenreService genreService;
    private final List<Integer> moviesToDelete = new CopyOnWriteArrayList<>();
    private final Map<Integer, SoftReference<Movie>> movieCache = new ConcurrentHashMap<>();
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();
    @Value("${executor.timeout}")
    private long timeout;

    @Autowired
    public DefaultMovieService(MovieDao movieDao, CurrencyService currencyService, CountryService countryService, GenreService genreService) {
        this.movieDao = movieDao;
        this.currencyService = currencyService;
        this.countryService = countryService;
        this.genreService = genreService;
    }

    @Override
    public List<Movie> getAll(MovieRequest movieRequest) {
        List<Movie> movies = movieDao.getAll(movieRequest);
        setGenresAndCountries(movies);
        return movies;
    }

    @Override
    public List<Movie> getRandomMovies() {
        List<Movie> movies = movieDao.getRandomMovies();
        setGenresAndCountries(movies);
        return movies;

    }

    @Override
    public List<Movie> getMoviesByGenre(int genreId, MovieRequest movieRequest) {
        List<Movie> movies = movieDao.getMoviesByGenre(genreId, movieRequest);
        setGenresAndCountries(movies);
        return movies;
    }

    private void setGenresAndCountries(List<Movie> movies) {
        List<Integer> movieIds = new ArrayList<>();
        for (Movie movie : movies) {
            movieIds.add(movie.getId());
            movie.setCountries(new ArrayList<>());
            movie.setGenres(new ArrayList<>());
        }
        List<Callable<Boolean>> tasks = new ArrayList<>();
        tasks.add(() -> countryService.enrich(movies, movieIds));
        tasks.add(() -> genreService.enrich(movies, movieIds));

        try {
            List<Future<Boolean>> futures = EXECUTOR_SERVICE.invokeAll(tasks, timeout, TimeUnit.SECONDS);
            for (Future<Boolean> future : futures) {
                if (future.isCancelled()) {
                    logger.error("Enrichment was cancelled");
                }
            }
        } catch (InterruptedException e) {
            logger.error("Enrichment was interrupted");
        }
    }

    @Override
    public Movie getMovieById(int movieId, Currency currency) {
        Movie movie;
        SoftReference<Movie> movieSoftReference = movieCache.get(movieId);
        if (movieSoftReference != null) {
            movie = movieSoftReference.get();
            if (movie == null) {
                movie = getMovieFromDB(movieId);
            }
        } else {
            movie = getMovieFromDB(movieId);
        }
        double rate = 1;
        if (currency == Currency.USD) {
            rate = currencyService.getRate("USD");
        } else if (currency == Currency.EUR) {
            rate = currencyService.getRate("EUR");
        }
        movie.setPrice(movie.getPrice() / rate);
        return movie;
    }

    @Transactional
    @Override
    public void editMovie(Movie movie) {
        genreService.removeAllGenresForMovie(Collections.singletonList(movie.getId()));
        countryService.removeAllCountriesForMovie(Collections.singletonList(movie.getId()));
        genreService.addGenresForMovie(movie.getGenres(), movie.getId());
        countryService.addCountriesForMovie(movie.getCountries(), movie.getId());
        movieDao.editMovie(movie);
        movieCache.put(movie.getId(), new SoftReference<>(movie));
    }

    @Transactional
    @Override
    public void addMovie(Movie movie) {
        int movieId = movieDao.addMovie(movie);
        genreService.addGenresForMovie(movie.getGenres(), movieId);
        countryService.addCountriesForMovie(movie.getCountries(), movieId);
    }

    @Override
    public void markMovieToDelete(int movieId) {
        moviesToDelete.add(movieId);
    }

    @Override
    public void unmarkMovieToDelete(int movieId) {
        moviesToDelete.removeIf(integer -> movieId == integer);
    }

    @Override
    public List<Movie> getMoviesByMask(String mask, MovieRequest movieRequest) {
        List<Movie> movies = movieDao.getMoviesByMask(mask, movieRequest);
        setGenresAndCountries(movies);
        return movies;
    }

    @Override
    public List<Movie> getMoviesForReport(ReportRequest reportRequest) {
        return movieDao.getMoviesForReport(reportRequest);
    }

    @Scheduled(cron = "59 59 23 * * ?")
    @Transactional
    void removeMovies() {
        movieDao.deleteMovie(moviesToDelete);
        genreService.removeAllGenresForMovie(moviesToDelete);
        countryService.removeAllCountriesForMovie(moviesToDelete);
        moviesToDelete.clear();
    }

    private Movie getMovieFromDB(int movieId) {
        Movie movie;
        movie = movieDao.getMovieById(movieId);
        List<Country> countries = countryService.getCountryForMovie(movie.getId());
        movie.setCountries(countries);
        List<Genre> genres = genreService.getGenreForMovie(movie.getId());
        movie.setGenres(genres);
        movieCache.put(movie.getId(), new SoftReference<>(movie));
        return movie;
    }
}
