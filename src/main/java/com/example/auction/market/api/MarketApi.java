package com.example.auction.market.api;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class MarketApi {
    private final WebClient webClient;
    private static final String internalServerErrorMsg = "Internal server error expected.";

    public Flux<String> getOptions() {
        return webClient.get()
                .uri("/markets/options")
                .retrieve()
                .bodyToFlux(String.class)
                .retry(3)
                .onErrorReturn(internalServerErrorMsg);
    }

    public Flux<String> getItemById(Integer id) {
        return webClient.get()
                .uri("/markets/items/" + id)
                .retrieve()
                .bodyToFlux(String.class)
                .retry(3)
                .onErrorReturn(internalServerErrorMsg);
    }

    public Flux<String> postItem(Map<String, Object> bodyMap) {
        return webClient.post()
                .uri("/markets/items")
                .bodyValue(bodyMap)
                .retrieve()
                .bodyToFlux(String.class)
                .retry(3)
                .onErrorReturn(internalServerErrorMsg);
    }
}
