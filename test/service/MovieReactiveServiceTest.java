package com.learnreactiveprogramming.service;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

class MovieReactiveServiceTest {
    private MovieInfoService movieInfoService=new MovieInfoService();
    private ReviewService reviewService=new ReviewService();
    private MovieReactiveService movieReactiveService=new MovieReactiveService(movieInfoService,reviewService);
    @Test
    void getAllMovies() {
        var moviesFlux=movieReactiveService.getAllMovies();
        StepVerifier.create(moviesFlux)
                .assertNext(movie -> {
                    assertEquals("Batman Begins",movie.getMovieInfo().getName());
                    assertEquals(2,movie.getReviewList().size());

                })
                .assertNext(movie -> {
                    assertEquals("The Dark Knight",movie.getMovieInfo().getName());
                    assertEquals(2,movie.getReviewList().size());

                })
                .assertNext(movie -> {
                    assertEquals("Dark Knight Rises",movie.getMovieInfo().getName());
                    assertEquals(2,movie.getReviewList().size());

                })
                .verifyComplete();
    }

    @Test
    void getMovieById() {
        long movieId=100l;
        var movieMono=movieReactiveService.getMovieById(movieId);
        StepVerifier.create(movieMono)
                .assertNext(movie -> {
                    assertEquals("Batman Begins",movie.getMovieInfo().getName());
                    assertEquals(2,movie.getReviewList().size());

                })

                .verifyComplete();

    }
}