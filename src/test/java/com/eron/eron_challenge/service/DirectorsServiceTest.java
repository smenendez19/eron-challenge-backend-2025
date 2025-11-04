package com.eron.eron_challenge.service;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eron.eron_challenge.client.WiremockClient;
import com.eron.eron_challenge.dto.response.DirectorsResponse;
import com.eron.eron_challenge.dto.response.MovieData;
import com.eron.eron_challenge.dto.response.WiremockMoviesResponse;

@ExtendWith(MockitoExtension.class)
@DisplayName("DirectorsService Tests")
class DirectorsServiceTest {

    @Mock
    private WiremockClient wiremockClient;

    @InjectMocks
    private DirectorsServiceImpl directorsService;

    private WiremockMoviesResponse mockResponsePage0;
    private WiremockMoviesResponse mockResponsePage1;
    private WiremockMoviesResponse mockResponsePage2;

    @BeforeEach
    void setUp() {
        // Mock página 0
        mockResponsePage0 = new WiremockMoviesResponse();
        mockResponsePage0.setPage(0);
        mockResponsePage0.setTotalPages(2);
        mockResponsePage0.setTotal(20);
        mockResponsePage0.setPerPage(10);
        mockResponsePage0.setData(Arrays.asList(
            createMovie("Movie 1", "Quentin Tarantino"),
            createMovie("Movie 2", "Quentin Tarantino"),
            createMovie("Movie 3", "Martin Scorsese"),
            createMovie("Movie 4", "Christopher Nolan"),
            createMovie("Movie 5", "Woody Allen")
        ));

        // Mock página 1
        mockResponsePage1 = new WiremockMoviesResponse();
        mockResponsePage1.setPage(1);
        mockResponsePage1.setTotalPages(2);
        mockResponsePage1.setTotal(20);
        mockResponsePage1.setPerPage(10);
        mockResponsePage1.setData(Arrays.asList(
            createMovie("Movie 6", "Quentin Tarantino"),
            createMovie("Movie 7", "Martin Scorsese"),
            createMovie("Movie 8", "Christopher Nolan"),
            createMovie("Movie 9", "Woody Allen"),
            createMovie("Movie 10", "Steven Spielberg")
        ));

        // Mock página 2
        mockResponsePage2 = new WiremockMoviesResponse();
        mockResponsePage2.setPage(2);
        mockResponsePage2.setTotalPages(2);
        mockResponsePage2.setTotal(20);
        mockResponsePage2.setPerPage(10);
        mockResponsePage2.setData(Arrays.asList(
            createMovie("Movie 11", "Quentin Tarantino"),
            createMovie("Movie 12", "Martin Scorsese"),
            createMovie("Movie 13", "Christopher Nolan"),
            createMovie("Movie 14", "Woody Allen"),
            createMovie("Movie 15", "James Cameron")
        ));
    }

    @Test
    @DisplayName("Should return directors with more than threshold movies")
    void testFetchData_WithThreshold2() {
        // Given
        int threshold = 2;
        when(wiremockClient.getMovies(0)).thenReturn(mockResponsePage0);
        when(wiremockClient.getMovies(1)).thenReturn(mockResponsePage1);
        when(wiremockClient.getMovies(2)).thenReturn(mockResponsePage2);

        // When
        DirectorsResponse result = directorsService.fetchData(threshold);

        // Then
        assertNotNull(result);
        assertNotNull(result.getDirectors());
        assertEquals(4, result.getDirectors().size());
        
        // Verificar que están ordenados alfabéticamente
        List<String> expectedDirectors = Arrays.asList(
            "Christopher Nolan",
            "Martin Scorsese", 
            "Quentin Tarantino",
            "Woody Allen"
        );
        assertEquals(expectedDirectors, result.getDirectors());

        // Verificar que se llamó a getMovies 3 veces (página 0, 1, 2)
        verify(wiremockClient, times(1)).getMovies(0);
        verify(wiremockClient, times(1)).getMovies(1);
        verify(wiremockClient, times(1)).getMovies(2);
    }

    @Test
    @DisplayName("Should return empty list when no directors exceed threshold")
    void testFetchData_NoDirectorsExceedThreshold() {
        // Given
        int threshold = 10;
        when(wiremockClient.getMovies(0)).thenReturn(mockResponsePage0);
        when(wiremockClient.getMovies(1)).thenReturn(mockResponsePage1);
        when(wiremockClient.getMovies(2)).thenReturn(mockResponsePage2);

        // When
        DirectorsResponse result = directorsService.fetchData(threshold);

        // Then
        assertNotNull(result);
        assertNotNull(result.getDirectors());
        assertTrue(result.getDirectors().isEmpty());
    }

    @Test
    @DisplayName("Should return all directors when threshold is 0")
    void testFetchData_WithThreshold0() {
        // Given
        int threshold = 0;
        when(wiremockClient.getMovies(0)).thenReturn(mockResponsePage0);
        when(wiremockClient.getMovies(1)).thenReturn(mockResponsePage1);
        when(wiremockClient.getMovies(2)).thenReturn(mockResponsePage2);

        // When
        DirectorsResponse result = directorsService.fetchData(threshold);

        // Then
        assertNotNull(result);
        assertNotNull(result.getDirectors());
        assertEquals(6, result.getDirectors().size());
        
        // Verificar orden alfabético
        List<String> directors = result.getDirectors();
        assertEquals("Christopher Nolan", directors.get(0));
        assertEquals("James Cameron", directors.get(1));
        assertEquals("Martin Scorsese", directors.get(2));
        assertEquals("Quentin Tarantino", directors.get(3));
        assertEquals("Steven Spielberg", directors.get(4));
        assertEquals("Woody Allen", directors.get(5));
    }

    @Test
    @DisplayName("Should handle single page response")
    void testFetchData_SinglePage() {
        // Given
        int threshold = 1;
        WiremockMoviesResponse singlePageResponse = new WiremockMoviesResponse();
        singlePageResponse.setPage(0);
        singlePageResponse.setTotalPages(0);
        singlePageResponse.setData(Arrays.asList(
            createMovie("Movie 1", "Director A"),
            createMovie("Movie 2", "Director A"),
            createMovie("Movie 3", "Director B")
        ));

        when(wiremockClient.getMovies(0)).thenReturn(singlePageResponse);

        // When
        DirectorsResponse result = directorsService.fetchData(threshold);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getDirectors().size());
        assertEquals("Director A", result.getDirectors().get(0));
        verify(wiremockClient, times(1)).getMovies(0);
    }

    @Test
    @DisplayName("Should throw exception when API call fails")
    void testFetchData_ApiCallFails() {
        // Given
        int threshold = 2;
        when(wiremockClient.getMovies(0)).thenThrow(new RuntimeException("API Error"));

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            directorsService.fetchData(threshold);
        });

        verify(wiremockClient, times(1)).getMovies(0);
    }

    private MovieData createMovie(String title, String director) {
        MovieData movie = new MovieData();
        movie.setTitle(title);
        movie.setDirector(director);
        movie.setYear("2020");
        movie.setGenre("Action");
        return movie;
    }
}