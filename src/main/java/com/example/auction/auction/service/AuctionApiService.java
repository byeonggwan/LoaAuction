package com.example.auction.auction.service;

import com.example.auction.auction.api.AuctionApi;
import com.example.auction.auction.domain.AuctionCategory;
import com.example.auction.auction.domain.AuctionItem;
import com.example.auction.auction.domain.AuctionItemDailyLog;
import com.example.auction.auction.domain.AuctionItemRtLog;
import com.example.auction.auction.repository.AuctionCategoryRepository;
import com.example.auction.auction.repository.AuctionItemRepository;
import com.example.auction.auction.repository.AuctionItemRtLogRepository;
import com.example.auction.market.domain.MarketCategory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuctionApiService {
    private final AuctionCategoryRepository auctionCategoryRepository;
    private final AuctionItemRepository auctionItemRepository;
    private final AuctionItemRtLogRepository auctionItemRtLogRepository;
    private final AuctionApi auctionApi;
    private final ObjectMapper objectMapper;

    /* TODO: 예시 보석
        {
          "ItemLevelMin": 0,
          "ItemLevelMax": 0,
          "ItemGradeQuality": null,
          "SkillOptions": [
            {
              "FirstOption": null,
              "SecondOption": null,
              "MinValue": null,
              "MaxValue": null
            }
          ],
          "EtcOptions": [
            {
              "FirstOption": null,
              "SecondOption": null,
              "MinValue": null,
              "MaxValue": null
            }
          ],
          "Sort": "BUY_PRICE",
          "CategoryCode": 210000,
          "CharacterClass": "",
          "ItemTier": null,
          "ItemGrade": "",
          "ItemName": "10레벨 멸화",
          "PageNo": 0,
          "SortCondition": "ASC"
        }

        5분마다 정보 가져오기? 한시간 12 * 하루 24 = 288
        가져온 뒤 하루마다 각 분기의 평균가의 평균가 내서 한 row로 저장?
        okay
     */
    public void scheduledTask() {
        insertItemsRealtimeLog().subscribe();
    }

    public Mono<Void> updateAll() {
        return updateCategories()
                .then();
    }

    private Mono<List<AuctionCategory>> updateCategories() {
        return getCategories()
                .doOnNext(auctionCategoryRepository::saveAll);
    }

    private Mono<List<AuctionCategory>> getCategories() {
        return auctionApi.getOptions()
                .map(string -> {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(string);
                        return parseCategories(jsonNode);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        return Collections.emptyList();
                    }
                });
    }

    private Mono<Void> insertItemsRealtimeLog() {
        List<AuctionItem> items = auctionItemRepository.findAll();

        return Flux.fromIterable(items)
                .concatMap(item -> Mono.just(item)
                        .delayElement(Duration.ofSeconds(1)) // 1초 간격
                        .flatMap(this::getItemRealtimeLog))
                .collectList()
                .doOnNext(logs -> {
                    auctionItemRtLogRepository.saveAll(logs);
                })
                .then();
    }

    private Mono<AuctionItemRtLog> getItemRealtimeLog(AuctionItem item) {
        return auctionApi.postItem(createPostBody(item))
                .map(string -> {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(string);
                        AuctionItemRtLog rtLog = parseItemRealtimeLog(item, jsonNode);
                        return rtLog;
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                        return new AuctionItemRtLog();
                    }
                });
    }

    private List<AuctionCategory> parseCategories(JsonNode jsonNode) {
        List<AuctionCategory> categories = new ArrayList<>();
        for (JsonNode categoryNode : jsonNode.get("Categories")) {
            Integer code = categoryNode.get("Code").asInt();
            String codeName = categoryNode.get("CodeName").asText();

            AuctionCategory auctionCategory = new AuctionCategory(code, codeName);
            categories.add(auctionCategory);
        }

        return categories;
    }

    private AuctionItemRtLog parseItemRealtimeLog(AuctionItem item, JsonNode jsonNode) {
        JsonNode logNodes = jsonNode.get("Items");
        int size = logNodes.size();
        Integer currentMinPrice = 0;
        Integer sumPrice = 0;

        if (!(logNodes != null && logNodes.isArray() && logNodes.size() > 0)) {
            throw new IllegalArgumentException();
        }

        currentMinPrice = logNodes.get(0).get("AuctionInfo").get("BuyPrice").asInt();
        for (JsonNode logNode : logNodes) {
            Integer buyPrice = logNode.get("AuctionInfo").get("BuyPrice").asInt();
            sumPrice += buyPrice;
        }
        BigDecimal avgPrice = BigDecimal.valueOf(sumPrice).divide(BigDecimal.valueOf(size));

        return new AuctionItemRtLog(item, currentMinPrice, avgPrice);
    }

    private Map<String, Object> createPostBody(AuctionItem item) {
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("Sort", "BUY_PRICE");
        bodyMap.put("CategoryCode", item.getCategory().getId());
        bodyMap.put("ItemName", item.getName());
        bodyMap.put("PageNo", 0);
        bodyMap.put("SortCondition", "ASC");
        System.out.println(bodyMap);
        return bodyMap;
    }

}
