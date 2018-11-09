package com.shalimov.movieland.dao.jdbc.mapper

import com.shalimov.movieland.entity.Genre
import org.junit.Test

import java.sql.ResultSet
import static org.junit.Assert.assertEquals
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

class GenreRowMapperTest {
    @Test
    void mapRow() {
        ResultSet resultSet = mock(ResultSet.class)
        when(resultSet.getInt("id")).thenReturn(1)
        when(resultSet.getString("name")).thenReturn("name")
        def expectedGenre = new Genre(1, "name")

        //when
        GenreRowMapper genreRowMapper = new GenreRowMapper()

        //then
        def actualGenre = genreRowMapper.mapRow(resultSet, 0)
        assertEquals(expectedGenre, actualGenre)
    }
}