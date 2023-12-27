package com.example.auction.market.api;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class MarketApi {
    private final WebClient webClient;
    private static final String internalServerErrorMsg = "Internal server error expected.";

    public Mono<String> getOptions() {
        return webClient.get()
                .uri("/markets/options")
                .retrieve()
                .bodyToMono(String.class)
                .retry(3)
                .onErrorReturn(internalServerErrorMsg);
    }

    public Mono<String> getItemById(Integer id) {
        return webClient.get()
                .uri("/markets/items/" + id)
                .retrieve()
                .bodyToMono(String.class)
                .retry(3)
                .onErrorReturn(internalServerErrorMsg);
    }

    public Mono<String> postItem(Map<String, Object> bodyMap) {
        return webClient.post()
                .uri("/markets/items")
                .bodyValue(bodyMap)
                .retrieve()
                .bodyToMono(String.class)
                .retry(3)
                .onErrorReturn(internalServerErrorMsg);
    }
}
