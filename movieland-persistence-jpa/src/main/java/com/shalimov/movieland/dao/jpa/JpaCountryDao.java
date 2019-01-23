package com.shalimov.movieland.dao.jpa;

import com.shalimov.movieland.dao.CountryDao;
import com.shalimov.movieland.dao.jpa.dto.CountryDto;
import com.shalimov.movieland.entity.Country;
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
public class JpaCountryDao implements CountryDao {

    private final SessionFactory sessionFactory;

    @Autowired
    public JpaCountryDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Country> getAll() {
        Query<CountryDto> countryDtoQuery = getCurrentSession().createQuery("select g from CountryDto g", CountryDto.class);
        List<CountryDto> countriesDto = countryDtoQuery.getResultList();
        return countryDtoConverter(countriesDto);
    }

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
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
