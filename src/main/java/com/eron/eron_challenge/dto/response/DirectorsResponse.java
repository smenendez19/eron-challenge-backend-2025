package com.eron.eron_challenge.dto.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response containing list of directors")
public class DirectorsResponse {
    
    @Schema(description = "List of director names", example = "[\"Quentin Tarantino\", \"Martin Scorsese\"]")
    private List<String> directors;

        public DirectorsResponse() {
    }

    public DirectorsResponse(List<String> directors) {
        this.directors = directors;
    }

    public List<String> getDirectors() {
        return directors;
    }
    
    public void setDirectors(List<String> directors) {
        this.directors = directors;
    }
}