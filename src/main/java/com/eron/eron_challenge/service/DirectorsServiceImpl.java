package com.eron.eron_challenge.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import com.eron.eron_challenge.client.WiremockClient;
import com.eron.eron_challenge.dto.response.DirectorsResponse;
import com.eron.eron_challenge.dto.response.MovieData;
import com.eron.eron_challenge.dto.response.WiremockMoviesResponse;

@Service
public class DirectorsServiceImpl implements DirectorsService {

    private final WiremockClient wiremockClient;
    private static final Logger logger = LoggerFactory.getLogger(DirectorsServiceImpl.class);

    public DirectorsServiceImpl(WiremockClient wiremockClient) {
        this.wiremockClient = wiremockClient;
    }

    @Override
    public DirectorsResponse fetchData(int threshold) {
        logger.info("Fetching directors with threshold: {}", threshold);

        List<MovieData> allMovies = new ArrayList<>();
        List<String> directors = new ArrayList<>();
        WiremockMoviesResponse response;
        int currPage = 0;
        int totalPages;


        // Obtener todas las peliculas de la API externa (Wiremock)
        // Page 0 devuelve cuantas paginas tiene en total
        response = wiremockClient.getMovies(currPage);
        totalPages = response.getTotalPages();
        allMovies.addAll(response.getData());

        while (currPage < totalPages) {
            currPage++;
            response = wiremockClient.getMovies(currPage);
            allMovies.addAll(response.getData());
        }

        // Contar cuantas peliculas tiene cada director
        Map<String, Integer> directorMovieCount = allMovies.stream()
                .collect(Collectors.groupingBy(MovieData::getDirector, Collectors.summingInt(e -> 1)));

        // Filtrar por umbral (mayor cantidad de peliculas dirigidas que el umbral)
        for (Map.Entry<String, Integer> entry : directorMovieCount.entrySet()) {
            String director = entry.getKey();
            int movieCount = entry.getValue();

            if (movieCount > threshold) {
                directors.add(director);
            }
        }

        // Ordenar alfabeticamente
        directors.sort(String::compareTo);

        logger.info("Found {} directors exceeding threshold", directors.size());

        return new DirectorsResponse(directors);
    }
}

