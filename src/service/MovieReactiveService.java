package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.domain.Movie;
import com.learnreactiveprogramming.domain.Review;
import com.learnreactiveprogramming.exception.MovieException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
@Slf4j
public class MovieReactiveService {

    private MovieInfoService movieInfoService;
    private ReviewService reviewService;

    public MovieReactiveService(MovieInfoService movieInfoService, ReviewService reviewService) {
        this.movieInfoService = movieInfoService;
        this.reviewService = reviewService;
    }


    public Flux<Movie> getAllMovies(){
        var movieInfoFlux=movieInfoService.retrieveMoviesFlux();
        return movieInfoFlux
                .flatMap(movieInfo -> {
                    Mono<List<Review>> reviewsMono= reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId())
                            .collectList();
                    return reviewsMono.map(reviewsList -> new Movie(movieInfo,reviewsList));

                })
                .onErrorMap((ex) -> {
                    log.error("Exception is : "+ex);
                    throw new MovieException(ex.getMessage());
                })
                .log();
    }

    public Mono<Movie> getMovieById(long movieId){
        var movieInfoMono=movieInfoService.retrieveMovieInfoMonoUsingId(movieId);
        var reviewsFlux=reviewService.retrieveReviewsFlux(movieId)
                .collectList();
        return movieInfoMono.zipWith(reviewsFlux,((movieInfo, reviews) -> new Movie(movieInfo,reviews))).log();

    }
}
