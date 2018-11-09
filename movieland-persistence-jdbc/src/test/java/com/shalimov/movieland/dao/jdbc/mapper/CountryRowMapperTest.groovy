package com.shalimov.movieland.dao.jdbc.mapper

import com.shalimov.movieland.entity.Country
import org.junit.Test

import java.sql.ResultSet
import static org.junit.Assert.assertEquals
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

class CountryRowMapperTest {
    @Test
    void mapRow() {
        ResultSet resultSet = mock(ResultSet.class)
        when(resultSet.getInt("id")).thenReturn(1)
        when(resultSet.getString("name")).thenReturn("name")
        def expectedCountry = new Country(1, "name")

        //when
        CountryRowMapper countryRowMapper = new CountryRowMapper()

        //then
        def actualCountry = countryRowMapper.mapRow(resultSet, 0)
        assertEquals(expectedCountry, actualCountry)
    }
}