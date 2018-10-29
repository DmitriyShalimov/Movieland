package com.shalimov.movieland.dao.jdbc.mapper;

import com.shalimov.movieland.entity.Country;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CountryRowMapper implements RowMapper<Country> {
    @Override
    public Country mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        int id=resultSet.getInt("id");
        String name=resultSet.getString("name");
        Country country = new Country(id, name);
        return country;
    }
}
