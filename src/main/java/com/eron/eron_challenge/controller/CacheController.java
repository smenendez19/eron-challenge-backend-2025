package com.eron.eron_challenge.controller;

import java.util.Objects;

import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cache")
public class CacheController {
    
    private final CacheManager cacheManager;
    
    public CacheController(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
    
    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCache() {
        Objects.requireNonNull(cacheManager.getCache("moviesCache")).clear();
        return ResponseEntity.ok("Cache cleared successfully.");
    }
}