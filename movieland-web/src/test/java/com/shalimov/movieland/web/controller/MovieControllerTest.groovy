package com.shalimov.movieland.web.controller

import com.shalimov.movieland.entity.Currency
import com.shalimov.movieland.entity.Movie
import com.shalimov.movieland.entity.MovieRequest
import com.shalimov.movieland.entity.User
import com.shalimov.movieland.entity.UserType
import com.shalimov.movieland.service.MovieService
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@RunWith(MockitoJUnitRunner.class)
class MovieControllerTest {
    @InjectMocks
    MovieController movieController

    @Mock
    MovieService movieService

    private MockMvc mockMvc
    private Movie movie


    @Before
    void setup() {
        movie = new Movie("nameRussian", "String nameNative", 2000, "description", 1.1, 1.1, "picturePath")
        List<Movie> movies = new ArrayList<>()
        movies.add(movie)
        Mockito.when(movieService.getAll((MovieRequest) Mockito.notNull())).thenReturn(movies)
        Mockito.when(movieService.getRandomMovies((MovieRequest) Mockito.notNull())).thenReturn(movies)
        Mockito.when(movieService.getMoviesByGenre(Mockito.anyInt(), (MovieRequest) Mockito.notNull())).thenReturn(movies)
        Mockito.when(movieService.getMovieById(1, Currency.USD)).thenReturn(movie)
        Mockito.when(movieService.addMovie((Movie) Mockito.notNull(), genres, countries)).thenReturn(true)
        Mockito.when(movieService.editMovie((Movie) Mockito.notNull())).thenReturn(true)
        MockitoAnnotations.initMocks(this)
        this.mockMvc = MockMvcBuilders.standaloneSetup(movieController).build()
    }

    @Test
    void getAllMovies() {
        def result = mockMvc.perform(get("/movie").param("rating", "ASC").param("price", "ASC"))
                .andExpect(status().isOk()).andReturn()
        def response = result.getResponse().getContentAsString()
        assertTrue(response.contains(movie.nameRussian))
    }

    @Test
    void getRandomMovies() {
        def result = mockMvc.perform(get("/movie/random").param("rating", "ASC").param("price", "ASC"))
                .andExpect(status().isOk()).andReturn()
        def response = result.getResponse().getContentAsString()
        assertTrue(response.contains(movie.nameRussian))
    }

    @Test
    void getMoviesByGenre() {
        def result = mockMvc.perform(get("/movie/genre/{id}", 1).param("rating", "ASC").param("price", "ASC"))
                .andExpect(status().isOk()).andReturn()
        def response = result.getResponse().getContentAsString()
        assertTrue(response.contains(movie.nameRussian))
    }

    @Test
    void getMovieById() {
        def result = mockMvc.perform(get("/movie/{id}", 1).param("currency", "USD"))
                .andExpect(status().isOk()).andReturn()
        def response = result.getResponse().getContentAsString()
        assertTrue(response.contains(movie.nameRussian))
    }

    @Test
    void addWithAdminRoleUser() {
        User user = new User()
        user.setUserType(UserType.ADMIN)
        RequestBuilder requestBuilder = post("/movie")
                .param("nameRussian", "nameRussian")
                .param("nameNative", "nameNative")
                .param("yearOfRelease", 1 as String)
                .param("description", "description")
                .param("picturePath", "picturePath")
                .param("rating", 1.1 as String)
                .param("price", 1.1 as String)
                .sessionAttr("loggedUser", user)
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
    }

    @Test
    void addWithUserRoleUser() {
        User user = new User()
        user.setUserType(UserType.USER)
        RequestBuilder requestBuilder = post("/movie")
                .param("nameRussian", "nameRussian")
                .param("nameNative", "nameNative")
                .param("yearOfRelease", 1 as String)
                .param("description", "description")
                .param("picturePath", "picturePath")
                .param("rating", 1.1 as String)
                .param("price", 1.1 as String)
                .sessionAttr("loggedUser", user)
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
    }

    @Test
    void editWithAdminRoleUser() {
        User user = new User()
        user.setUserType(UserType.ADMIN)
        RequestBuilder requestBuilder = put("/movie/{movieId}", 1)
                .param("nameRussian", "nameRussian")
                .param("nameNative", "nameNative")
                .param("yearOfRelease", 1 as String)
                .param("description", "description")
                .param("picturePath", "picturePath")
                .param("rating", 1.1 as String)
                .param("price", 1.1 as String)
                .sessionAttr("loggedUser", user)
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
    }

    @Test
    void editWithUserRoleUser() {
        User user = new User()
        user.setUserType(UserType.USER)
        RequestBuilder requestBuilder = put("/movie/{movieId}", 1)
                .param("nameRussian", "nameRussian")
                .param("nameNative", "nameNative")
                .param("yearOfRelease", 1 as String)
                .param("description", "description")
                .param("picturePath", "picturePath")
                .param("rating", 1.1 as String)
                .param("price", 1.1 as String)
                .sessionAttr("loggedUser", user)
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
    }

}