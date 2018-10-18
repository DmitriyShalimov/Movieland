package com.shalimov.movieland.dao.jdbc;

import com.shalimov.movieland.dao.CountryDao;
import com.shalimov.movieland.dao.jdbc.mapper.CountryRowMapper;
import com.shalimov.movieland.entity.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class JdbcCountryDao implements CountryDao {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final RowMapper<Country> COUNTRY_ROW_MAPPER = new CountryRowMapper();

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }
}
