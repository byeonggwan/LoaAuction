package com.example.auction.market.api;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class MarketApi {
    private final WebClient webClient;
    // TODO: 429 TOO MANY REQ 고려하기
    public Mono<String> getOptions() {
        System.out.println("GET");
        return makeGetRequest("/markets/options");
    }

    public Mono<String> getItemById(Integer id) {
        System.out.println("GET");
        return makeGetRequest("/markets/items/" + id);
    }

    public Mono<String> postItem(Map<String, Object> bodyMap) {
        System.out.println("POST");
        return makePostRequest("/markets/items", bodyMap);
    }

    private Mono<String> makeGetRequest(String uri) {
        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
                .retry(3);
    }

    private Mono<String> makePostRequest(String uri, Map<String, Object> bodyMap) {
        return webClient.post()
                .uri(uri)
                .bodyValue(bodyMap)
                .retrieve()
                .bodyToMono(String.class)
                .retry(3);
    }
}
