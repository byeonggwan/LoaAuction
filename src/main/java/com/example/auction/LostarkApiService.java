package com.example.auction;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class LostarkApiService {
    private WebClient webClient;

    @Autowired
    public LostarkApiService(WebClient webClient) {
        this.webClient = webClient;
    }

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
