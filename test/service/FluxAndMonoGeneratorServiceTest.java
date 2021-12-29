package com.learnreactiveprogramming.service;

import com.learnreactiveprogramming.exception.ReactorException;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;
import java.util.List;

public class FluxAndMonoGeneratorServiceTest {
    FluxAndMonoGeneratorService fluxAndMonoGeneratorService=new FluxAndMonoGeneratorService();
    @Test
    void namesFlux(){
        var namesFlux=fluxAndMonoGeneratorService.namesFlux();
        StepVerifier.create(namesFlux)
               // .expectNext("alex", "ben", "chloe")
               // .expectNextCount(3)
                .expectNext("alex")
                .expectNextCount(2)
                .verifyComplete();

    }
    @Test
    void namesFlux_map(){
        int stringLength=3;
        var flux=fluxAndMonoGeneratorService.namesFlux_map(stringLength);
        StepVerifier.create(flux)
                 .expectNext("4-ALEX", "5-CHLOE")
                 .verifyComplete();

    }
    @Test
    void namesFlux_flatmap(){
        int stringLength=3;
        var namesFlux=fluxAndMonoGeneratorService.namesFlux_flatmap(stringLength);
        StepVerifier.create(namesFlux)
                .expectNext("A","L" ,"E","X","C","H","L","O","E")
                .verifyComplete();

    }

    @Test
    void namesFlux_immutibility() {
        var flux=fluxAndMonoGeneratorService.namesFlux_immutibility();
        StepVerifier.create(flux)
                .expectNext("alex", "ben", "chloe")
                .verifyComplete();

    }

    @Test
    void namesFlux_flatmap_async() {
        int stringLength=3;
        var namesFlux=fluxAndMonoGeneratorService.namesFlux_flatmap_async(stringLength);
        StepVerifier.create(namesFlux)
                //.expectNext("A","L" ,"E","X","C","H","L","O","E")
                .expectNextCount(9)
                .verifyComplete();
    }
    @Test
    void namesFlux_concatmap() {
        int stringLength=3;
        var namesFlux=fluxAndMonoGeneratorService.namesFlux_concatmap(stringLength);
        StepVerifier.create(namesFlux)
                .expectNext("A","L" ,"E","X","C","H","L","O","E")
                //.expectNextCount(9)
                .verifyComplete();
    }
    @Test
    void namesFlux_concatmap_virtualTimer() {
        VirtualTimeScheduler.getOrSet();
        int stringLength=3;
        var namesFlux=fluxAndMonoGeneratorService.namesFlux_concatmap(stringLength);
        StepVerifier.withVirtualTime(()->namesFlux)
                .thenAwait(Duration.ofSeconds(10))
                .expectNext("A","L" ,"E","X","C","H","L","O","E")
                //.expectNextCount(9)
                .verifyComplete();
    }

    @Test
    void namesMono_map_filter() {
        int stringLength=3;
        var namesMonoFlux=fluxAndMonoGeneratorService.namesMono_map_filter(stringLength);
        StepVerifier.create(namesMonoFlux)
                .expectNext("ALEX")
                //.expectNextCount(9)
                .verifyComplete();
    }

    @Test
    void namesMono_flatMap() {
        int stringLength=3;
        var namesMonoFlux=fluxAndMonoGeneratorService.namesMono_flatMap(stringLength);
        StepVerifier.create(namesMonoFlux)
                .expectNext(List.of("A","L" ,"E","X"))
                //.expectNextCount(9)
                .verifyComplete();
    }

    @Test
    void namesMono_flatMapMany() {
        int stringLength=3;
        var namesMonoFlux=fluxAndMonoGeneratorService.namesMono_flatMapMany(stringLength);
        StepVerifier.create(namesMonoFlux)
                .expectNext("A","L" ,"E","X")
                //.expectNextCount(9)
                .verifyComplete();
    }

    @Test
    void namesFlux_transform() {
        int stringLength=3;
        var namesFlux=fluxAndMonoGeneratorService.namesFlux_transform(stringLength);
        StepVerifier.create(namesFlux)
                .expectNext("A","L" ,"E","X","C","H","L","O","E")
                .verifyComplete();
    }
    @Test
    void namesFlux_transform_1() {
        int stringLength=6;
        var namesFlux=fluxAndMonoGeneratorService.namesFlux_transform(stringLength);
        StepVerifier.create(namesFlux)
                .expectNext("default")
                .verifyComplete();
    }

    @Test
    void namesFlux_transform_switch_ifEmpty() {
        int stringLength=6;
        var namesFlux=fluxAndMonoGeneratorService.namesFlux_transform_switch_ifEmpty(stringLength);
        StepVerifier.create(namesFlux)
                .expectNext("D","E","F","A","U","L","T")
                .verifyComplete();
    }

    @Test
    void explore_concat() {
        var concatFlux=fluxAndMonoGeneratorService.explore_concat();
        StepVerifier.create(concatFlux)
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }

    @Test
    void explore_concatwith() {
        var concatFlux=fluxAndMonoGeneratorService.explore_concatwith();
        StepVerifier.create(concatFlux)
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }

    @Test
    void explore_concatwith_mono() {
        var concatWithMono=fluxAndMonoGeneratorService.explore_concatwith_mono();
        StepVerifier.create(concatWithMono)
                .expectNext("A","B")
                .verifyComplete();
    }

    @Test
    void explore_merge() {
        var mergeFlux=fluxAndMonoGeneratorService.explore_merge();
        StepVerifier.create(mergeFlux)
                .expectNext("A","D","B","E","C","F")
                .verifyComplete();
    }

    @Test
    void explore_mergeWith() {
        var mergeWithFlux=fluxAndMonoGeneratorService.explore_mergeWith();
        StepVerifier.create(mergeWithFlux)
                .expectNext("A","D","B","E","C","F")
                .verifyComplete();
    }

    @Test
    void explore_mergeWith_mono() {
        var mergeWithMono=fluxAndMonoGeneratorService.explore_mergeWith_mono();
        StepVerifier.create(mergeWithMono)
                .expectNext("A","B")
                .verifyComplete();
    }

    @Test
    void explore_mergeSequential() {
        var mergeSequentialFlux=fluxAndMonoGeneratorService.explore_mergeSequential();
        StepVerifier.create(mergeSequentialFlux)
                .expectNext("A","B","C","D","E","F")
                .verifyComplete();
    }

    @Test
    void explore_zip() {
        var zipFlux=fluxAndMonoGeneratorService.explore_zip();
        StepVerifier.create(zipFlux)
                .expectNext("AD","BE","CF")
                .verifyComplete();
    }

    @Test
    void explore_zip_1() {
        var value=fluxAndMonoGeneratorService.explore_zip_1();
        StepVerifier.create(value)
                .expectNext("AD14","BE25","CF36")
                .verifyComplete();
    }

    @Test
    void explore_zipWith() {
        var zipWithFlux=fluxAndMonoGeneratorService.explore_zipWith();
        StepVerifier.create(zipWithFlux)
                .expectNext("AD","BE","CF")
                .verifyComplete();
    }

    @Test
    void explore_zipWith_mono() {
        var zipWithMono=fluxAndMonoGeneratorService.explore_zipWith_mono();
        StepVerifier.create(zipWithMono)
                .expectNext("AB")
                .verifyComplete();
    }

    @Test
    void exception_flux() {
        var value=fluxAndMonoGeneratorService.exception_flux();
        StepVerifier.create(value)
                .expectNext("A","B","C")
                .expectError(RuntimeException.class)
                .verify();

    }
    @Test
    void exception_flux_1() {
        var value=fluxAndMonoGeneratorService.exception_flux();
        StepVerifier.create(value)
                .expectNext("A","B","C")
                .expectError()
                .verify();

    }
    @Test
    void exception_flux_2() {
        var value=fluxAndMonoGeneratorService.exception_flux();
        StepVerifier.create(value)
                .expectNext("A","B","C")
                .expectErrorMessage("Exception Occurred")
                .verify();

    }

    @Test
    void explore_OnErrorReturn() {
        var value=fluxAndMonoGeneratorService.explore_OnErrorReturn();
        StepVerifier.create(value)
                .expectNext("A","B","C","D")
                //.expectNext("A","B","C","D","E") //test will run successfully
                .verifyComplete();
    }

    @Test
    void explore_OnErrorResume() {
        var e=new IllegalStateException("Not a valid State");
        var value=fluxAndMonoGeneratorService.explore_OnErrorResume(e);
        StepVerifier.create(value)
                .expectNext("A","B","C","D","E","F")
                //.expectNext("A","B","C","D","E","F","G")//test will run successfully
                .verifyComplete();
    }
    @Test
    void explore_OnErrorResume_1() {
        var e=new RuntimeException("Not a valid State");
        var value=fluxAndMonoGeneratorService.explore_OnErrorResume(e);
        StepVerifier.create(value)
                .expectNext("A","B","C")
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void explore_OnErrorContinue() {
        var value=fluxAndMonoGeneratorService.explore_OnErrorContinue();
        StepVerifier.create(value)
                .expectNext("A","C","D")
                .verifyComplete();
    }

    @Test
    void explore_OnErrorMap() {
        var value=fluxAndMonoGeneratorService.explore_OnErrorMap();
        StepVerifier.create(value)
                .expectNext("A")
                .expectError(ReactorException.class)
                .verify();
    }

    @Test
    void explore_doOnError() {
        var value=fluxAndMonoGeneratorService.explore_doOnError();
        StepVerifier.create(value)
                .expectNext("A","B","C","D")
                .expectError(IllegalStateException.class)
                .verify();
    }

    @Test
    void explore_Mono_onErrorReturn() {
        var value=fluxAndMonoGeneratorService.explore_Mono_onErrorReturn();
        StepVerifier.create(value)
                .expectNext("abc")
                .verifyComplete();
    }
}
