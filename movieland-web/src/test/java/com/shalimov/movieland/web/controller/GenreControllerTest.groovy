package com.shalimov.movieland.web.controller

import com.shalimov.movieland.entity.Genre
import com.shalimov.movieland.service.GenreService
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.runners.MockitoJUnitRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders

import static org.junit.Assert.assertTrue
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@RunWith(MockitoJUnitRunner.class)
 class GenreControllerTest {
    @InjectMocks
    GenreController genreController

    @Mock
    GenreService genreService

    private MockMvc mockMvc
    private Genre genre

    @Test
    void getAllGenres() {
        genre = new Genre(1, 'genre')
        List<Genre> genres = new ArrayList<>()
        genres.add(genre)
        Mockito.when(genreService.getAll()).thenReturn(genres)
        MockitoAnnotations.initMocks(this)
        this.mockMvc = MockMvcBuilders.standaloneSetup(genreController).build()
        def result = mockMvc.perform(get("/genre"))
                .andExpect(status().isOk()).andReturn()
        def response = result.getResponse().getContentAsString()
        assertTrue(response.contains(genre.title))
    }
}