package com.eron.eron_challenge.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.eron.eron_challenge.dto.response.WiremockMoviesResponse;

@Component
public class WiremockClient {
    
    private final RestTemplate restTemplate;
    
    @Value("${external.api.url:https://wiremock.dev.eroninternational.com/api}")
    private String apiBaseUrl;
    
    public WiremockClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    @Cacheable(value = "moviesCache", key = "#page")
    public WiremockMoviesResponse getMovies(int page) {
        try {
            String url = apiBaseUrl + "/movies/search?page=" + page;
            WiremockMoviesResponse response = restTemplate.getForObject(url, WiremockMoviesResponse.class);
            
            if (response == null) {
                throw new RuntimeException("Cannot get movies from external API.");
            }
            return response;
        } catch (RestClientException e) {
            throw new RuntimeException("Error fetching movies from external API: " + e.getMessage(), e);
        }
    }
}