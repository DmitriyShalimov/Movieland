package com.shalimov.movieland.web.controller

import com.shalimov.movieland.entity.User
import com.shalimov.movieland.service.SecurityService
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.runners.MockitoJUnitRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.RequestBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import static org.junit.Assert.assertTrue

import javax.servlet.http.HttpSession


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@RunWith(MockitoJUnitRunner.class)
class UserControlerTest {

    @InjectMocks
    UserController userController

    @Mock
    private SecurityService securityService
    private MockMvc mockMvc
    private UUID uuid

    @Before
    void setup() {
        uuid = UUID.randomUUID()
        User user = new User(uuid: uuid, nickName: "name")
        Mockito.when(securityService.authenticate(Mockito.anyString(), Mockito.anyString(), (HttpSession) Mockito.notNull())).thenReturn(Optional.of(user))
        MockitoAnnotations.initMocks(this)
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build()
    }


    @Test
    void login() {
        def expectedUuid = "\"uuid\":\"" + uuid + "\""
        def expectedNickname = "\"nickName\":\"name\""
        RequestBuilder requestBuilder = post("/login")
                .param("email", 'text')
                .param("password", 'text')
        def result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk()).andReturn()
        def actualResponse = result.getResponse().getContentAsString()
        //assertEquals(expectedResponse, actualResponse)

        assertTrue(actualResponse.contains(expectedUuid))
        assertTrue(actualResponse.contains(expectedNickname))
    }


}
