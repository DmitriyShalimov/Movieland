package com.shalimov.movieland.web.controller

import com.shalimov.movieland.entity.Country
import com.shalimov.movieland.service.CountryService
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
class CountryControllerTest {
    @InjectMocks
    CountryController countryController

    @Mock
    CountryService countryService

    private MockMvc mockMvc
    private Country country

    @Test
    void getAllCountries() {
        country = new Country(1, 'country')
        List<Country> countries = new ArrayList<>()
        countries.add(country)
        Mockito.when(countryService.getAll()).thenReturn(countries)
        MockitoAnnotations.initMocks(this)
        this.mockMvc = MockMvcBuilders.standaloneSetup(countryController).build()
        def result = mockMvc.perform(get("/country"))
                .andExpect(status().isOk()).andReturn()
        def response = result.getResponse().getContentAsString()
        assertTrue(response.contains(country.name))
    }
}