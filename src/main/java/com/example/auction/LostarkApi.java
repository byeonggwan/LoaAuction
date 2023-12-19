package com.example.auction;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class LostarkApi {
    private final WebClient webClient;

    public void getOptions() {
        Flux<String> request = webClient.get()
                .uri("/auctions/options")
                .retrieve()
                .bodyToFlux(String.class)
                .retry(3)
                .onErrorReturn("Internal server error expected.");

        request.subscribe(string -> {
            // store it to Database
            System.out.println(string);
        });
    }
}
