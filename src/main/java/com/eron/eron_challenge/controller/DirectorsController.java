package com.eron.eron_challenge.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.constraints.Min;

import com.eron.eron_challenge.dto.response.DirectorsResponse;
import com.eron.eron_challenge.service.DirectorsService;

@RestController
@RequestMapping("/api/directors")
@Validated
@Tag(name = "Directors", description = "Directors API")
public class DirectorsController {

    private final DirectorsService directorsService;

    public DirectorsController(DirectorsService directorsService) {
        this.directorsService = directorsService;
    }

    @GetMapping
    @Operation(
        summary = "Get directors by threshold",
        description = "Returns directors who have directed more than the specified threshold of movies"
    )
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "400", description = "Invalid threshold parameter")
    public ResponseEntity<DirectorsResponse> getDirectors(
            @Parameter(description = "Minimum number of movies directed", required = true)
            @RequestParam(name = "threshold") 
            @Min(value = 0, message = "Threshold must be non-negative") 
            int threshold) {

        DirectorsResponse data = directorsService.fetchData(threshold);
        return ResponseEntity.ok(data);
    }
}