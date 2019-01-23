package com.shalimov.movieland.dao.jpa;

import com.shalimov.movieland.dao.GenreDao;
import com.shalimov.movieland.dao.jpa.dto.GenreDto;

import com.shalimov.movieland.entity.Genre;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class JpaGenreDao implements GenreDao {


    private final SessionFactory sessionFactory;

    @Autowired
    public JpaGenreDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public List<Genre> getAll() {
        Query<GenreDto> genreDtoQuery = getCurrentSession().createQuery("select g from GenreDto g", GenreDto.class);
        List<GenreDto> genresDto = genreDtoQuery.getResultList();
        return genreDtoConverter(genresDto);
    }



    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    private List<Genre> genreDtoConverter(List<GenreDto> genresDto) {
        List<Genre> genres = new ArrayList<>();
        for (GenreDto genreDto : genresDto) {
            Genre genre = new Genre(genreDto.getId(), genreDto.getTitle());
            genres.add(genre);
        }
        return genres;
    }
}
