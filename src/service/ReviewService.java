package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.domain.MovieInfo;
import com.learnreactiveprogramming.domain.Review;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

import java.util.List;

public class ReviewService {

    private WebClient webClient;

    public ReviewService(WebClient webClient) {
        this.webClient = webClient;
    }

    public ReviewService() {
    }

    public  List<Review> retrieveReviews(long movieInfoId){

        return List.of(new Review(1L, movieInfoId, "Awesome Movie", 8.9),
                new Review(2L, movieInfoId, "Excellent Movie", 9.0));
    }

    public Flux<Review> retrieveReviewsFlux_RestClient(long movieInfoId){

       var uri= UriComponentsBuilder.fromUriString("/v1/reviews")
                .queryParam("movieinfoId",movieInfoId)
                .buildAndExpand()
                .toUriString();
       return  webClient.get().uri(uri)
                .retrieve()
                .bodyToFlux(Review.class);
    }

    public Flux<Review> retrieveReviewsFlux(long movieInfoId){

        var reviewsList = List.of(new Review(1L,movieInfoId, "Awesome Movie", 8.9),
                new Review(2L, movieInfoId, "Excellent Movie", 9.0));
        return Flux.fromIterable(reviewsList);
    }

}
