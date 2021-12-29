package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.domain.Movie;
import com.learnreactiveprogramming.domain.Review;
import com.learnreactiveprogramming.exception.MovieException;
import com.learnreactiveprogramming.exception.NetworkException;
import com.learnreactiveprogramming.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

import java.time.Duration;
import java.util.List;
@Slf4j
public class MovieReactiveService {

    private MovieInfoService movieInfoService;
    private ReviewService reviewService;
    private RevenueService revenueService;

    public MovieReactiveService(MovieInfoService movieInfoService, ReviewService reviewService) {
        this.movieInfoService = movieInfoService;
        this.reviewService = reviewService;
    }

    public MovieReactiveService(MovieInfoService movieInfoService, ReviewService reviewService, RevenueService revenueService) {
        this.movieInfoService = movieInfoService;
        this.reviewService = reviewService;
        this.revenueService = revenueService;
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
    public Flux<Movie> getAllMovies_restClient(){
        var movieInfoFlux=movieInfoService.retrieveAllMovieInfo_RestClient();
        return movieInfoFlux
                .flatMap(movieInfo -> {
                    Mono<List<Review>> reviewsMono= reviewService.retrieveReviewsFlux_RestClient(movieInfo.getMovieInfoId())
                            .collectList();
                    return reviewsMono.map(reviewsList -> new Movie(movieInfo,reviewsList));

                })
                .onErrorMap((ex) -> {
                    log.error("Exception is : "+ex);
                    throw new MovieException(ex.getMessage());
                })
                .log();
    }
    public Flux<Movie> getAllMovies_retryWhen(){
        //instead of backof can use fixedDealy() also
        var movieInfoFlux=movieInfoService.retrieveMoviesFlux();
        return movieInfoFlux
                .flatMap(movieInfo -> {
                    Mono<List<Review>> reviewsMono= reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId())
                            .collectList();
                    return reviewsMono.map(reviewsList -> new Movie(movieInfo,reviewsList));

                })
                .onErrorMap((ex) -> {
                    log.error("Exception is : "+ex);
                    if(ex instanceof NetworkException)
                        throw new MovieException(ex.getMessage());
                    else
                        throw new ServiceException(ex.getMessage());

                })
                .retryWhen(getRetryBackoffSpec())
                .log();
    }
    public Flux<Movie> getAllMovies_repeat(){
        //instead of backof can use fixedDealy() also
        var movieInfoFlux=movieInfoService.retrieveMoviesFlux();
        return movieInfoFlux
                .flatMap(movieInfo -> {
                    Mono<List<Review>> reviewsMono= reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId())
                            .collectList();
                    return reviewsMono.map(reviewsList -> new Movie(movieInfo,reviewsList));

                })
                .onErrorMap((ex) -> {
                    log.error("Exception is : "+ex);
                    if(ex instanceof NetworkException)
                        throw new MovieException(ex.getMessage());
                    else
                        throw new ServiceException(ex.getMessage());

                })
                .retryWhen(getRetryBackoffSpec())
                .repeat()
                .log();
    }
    public Flux<Movie> getAllMovies_repeat_n(long n){
        //instead of backof can use fixedDealy() also
        var movieInfoFlux=movieInfoService.retrieveMoviesFlux();
        return movieInfoFlux
                .flatMap(movieInfo -> {
                    Mono<List<Review>> reviewsMono= reviewService.retrieveReviewsFlux(movieInfo.getMovieInfoId())
                            .collectList();
                    return reviewsMono.map(reviewsList -> new Movie(movieInfo,reviewsList));

                })
                .onErrorMap((ex) -> {
                    log.error("Exception is : "+ex);
                    if(ex instanceof NetworkException)
                        throw new MovieException(ex.getMessage());
                    else
                        throw new ServiceException(ex.getMessage());

                })
                .retryWhen(getRetryBackoffSpec())
                .repeat(n)
                .log();
    }
    private RetryBackoffSpec getRetryBackoffSpec() {
        var retryWhen= Retry.backoff(3, Duration.ofMillis(500))
                .filter(ex -> ex instanceof MovieException)
                .onRetryExhaustedThrow(((retryBackoffSpec, retrySignal) -> Exceptions.propagate(retrySignal.failure())));
        return retryWhen;
    }

    public Flux<Movie> getAllMovies_retry(){
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
                .retry(3)
                .log();
    }
    public Mono<Movie> getMovieById(long movieId){
        var movieInfoMono=movieInfoService.retrieveMovieInfoMonoUsingId(movieId);
        var reviewsFlux=reviewService.retrieveReviewsFlux(movieId)
                .collectList();
        return movieInfoMono.zipWith(reviewsFlux,((movieInfo, reviews) -> new Movie(movieInfo,reviews))).log();

    }
    public Mono<Movie> getMovieById_RestClient(long movieId){
        var movieInfoMono=movieInfoService.retrieveMovieInfoMonoUsingId(movieId);
        var reviewsFlux=reviewService.retrieveReviewsFlux_RestClient(movieId)
                .collectList();
        return movieInfoMono.zipWith(reviewsFlux,((movieInfo, reviews) -> new Movie(movieInfo,reviews))).log();

    }
    public Mono<Movie> getMovieById_withRevenue(long movieId){
        var movieInfoMono=movieInfoService.retrieveMovieInfoMonoUsingId(movieId);
        var reviewsFlux=reviewService.retrieveReviewsFlux(movieId)
                .collectList();
        var revenueMono=Mono.fromCallable(()-> revenueService.getRevenue(movieId))
                .subscribeOn(Schedulers.boundedElastic());

        return movieInfoMono.zipWith(reviewsFlux,((movieInfo, reviews) -> new Movie(movieInfo,reviews)))
                .zipWith(revenueMono,(movie,revenue) -> {
                    movie.setRevenue(revenue);
                    return movie;
                }).log();

    }
}
