package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class ReviewServiceTest {
    WebClient webClient =WebClient.builder()
            .baseUrl("http://localhost:8080/movies")
            .build();
    ReviewService reviewService=new ReviewService(webClient);
    @Test
    void retrieveReviewsFlux_RestClient() {
        var movieInfoId=1L;
        var reviewsFlux=reviewService.retrieveReviewsFlux_RestClient(movieInfoId);
        StepVerifier.create(reviewsFlux)
                .expectNextCount(1)
                .thenCancel();
    }
}