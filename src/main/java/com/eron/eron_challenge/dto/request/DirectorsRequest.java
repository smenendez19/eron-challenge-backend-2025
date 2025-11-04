package com.eron.eron_challenge.dto.request;

public class DirectorsRequest {
    
    private int threshold;
    
    public DirectorsRequest() {
    }
    
    public DirectorsRequest(int threshold) {
        this.threshold = threshold;
    }
    
    public int getThreshold() {
        return threshold;
    }
    
    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }
}