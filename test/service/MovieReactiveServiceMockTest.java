package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.exception.MovieException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class MovieReactiveServiceMockTest {
    @Mock
    private MovieInfoService movieInfoService;
    @Mock
    private ReviewService reviewService;
    @InjectMocks
    private  MovieReactiveService retrieveMovieService;
    @Test
    void getAllMovies() {

        Mockito.when(movieInfoService.retrieveMoviesFlux())
                .thenCallRealMethod();
        Mockito.when(reviewService.retrieveReviewsFlux(anyLong()))
                .thenCallRealMethod();
        var moviesFlux= retrieveMovieService.getAllMovies();

        StepVerifier.create(moviesFlux)
                .expectNextCount(3)
                .verifyComplete();
    }
    @Test
    void getAllMovies_1() {
        var errorMessage="Exception occurred in ReviewService";
        Mockito.when(movieInfoService.retrieveMoviesFlux())
                .thenCallRealMethod();
        Mockito.when(reviewService.retrieveReviewsFlux(anyLong()))
                .thenThrow(new RuntimeException(errorMessage));
        var moviesFlux= retrieveMovieService.getAllMovies();

        StepVerifier.create(moviesFlux)
                .expectError(MovieException.class)
                .verify();
    }
}