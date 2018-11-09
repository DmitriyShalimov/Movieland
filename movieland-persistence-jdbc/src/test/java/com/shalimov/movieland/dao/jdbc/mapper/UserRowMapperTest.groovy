package com.shalimov.movieland.dao.jdbc.mapper

import com.shalimov.movieland.entity.User
import com.shalimov.movieland.entity.UserType
import org.junit.Test

import java.sql.ResultSet
import static org.junit.Assert.assertEquals
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

class UserRowMapperTest {

    @Test
    void mapRow() {
        ResultSet resultSet = mock(ResultSet.class)
        when(resultSet.getInt("id")).thenReturn(1)
        when(resultSet.getString("firstname")).thenReturn("firstname")
        when(resultSet.getString("lastname")).thenReturn("lastname")
        when(resultSet.getString("email")).thenReturn("email")
        when(resultSet.getString("role")).thenReturn("U")
        when(resultSet.getString("password")).thenReturn("password")
        when(resultSet.getString("salt")).thenReturn("salt")

        def expectedUser = new User()
        expectedUser.setId(1)
        expectedUser.setNickName("firstname lastname")
        expectedUser.setEmail("email")
        expectedUser.setPassword("password")
        expectedUser.setSalt("salt")
        expectedUser.setUserType(UserType.USER)

        //when
        UserRowMapper userRowMapper = new UserRowMapper()

        //then
        def actualUser = userRowMapper.mapRow(resultSet, 0)
        assertEquals(expectedUser, actualUser)
    }
}