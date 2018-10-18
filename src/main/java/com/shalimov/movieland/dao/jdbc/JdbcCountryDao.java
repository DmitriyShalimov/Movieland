package com.shalimov.movieland.dao.jdbc;

import com.shalimov.movieland.dao.CountryDao;
import com.shalimov.movieland.dao.jdbc.mapper.CountryRowMapper;
import com.shalimov.movieland.entity.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

public class JdbcCountryDao implements CountryDao {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final RowMapper<Country> COUNTRY_ROW_MAPPER = new CountryRowMapper();
    private String getAllCountriesSql;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<Country> getAll() {
        logger.info("start receiving all countries");
        return namedParameterJdbcTemplate.query(getAllCountriesSql, COUNTRY_ROW_MAPPER);
    }

    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public void setGetAllCountriesSql(String getAllCountriesSql) {
        this.getAllCountriesSql = getAllCountriesSql;
    }
}
