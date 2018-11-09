package com.shalimov.movieland.web.controller

import com.shalimov.movieland.entity.User
import com.shalimov.movieland.entity.UserType
import com.shalimov.movieland.service.ReviewService

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@RunWith(MockitoJUnitRunner.class)
class ReviewControllerTest {
    @InjectMocks
    ReviewController reviewController

    @Mock
    ReviewService reviewService

    private MockMvc mockMvc

    @Test
    void reviewWithoutLoginUser() {
        User user = new User()
        user.setId(1)
        Mockito.when(reviewService.addReview(1, "text", 1)).thenReturn(true)
        MockitoAnnotations.initMocks(this)
        this.mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build()
        RequestBuilder requestBuilder = post("/review")
                .param("movieId", 1 as String)
                .param("text", 'text')
                .sessionAttr("loggedUser", user)
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
    }

    @Test
    void reviewWithLoginUser() {
        User user = new User()
        user.setId(1)
        user.setUserType(UserType.USER)
        Mockito.when(reviewService.addReview(1, "text", 1)).thenReturn(true)
        MockitoAnnotations.initMocks(this)
        this.mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build()
        RequestBuilder requestBuilder = post("/review")
                .param("movieId", 1 as String)
                .param("text", 'text')
                .sessionAttr("loggedUser", user)
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
    }
}