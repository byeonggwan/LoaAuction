package com.example.auction.market.service;

import com.example.auction.market.api.MarketApi;
import com.example.auction.market.domain.MarketCategory;
import com.example.auction.market.domain.MarketItem;
import com.example.auction.market.domain.MarketItemDailyLog;
import com.example.auction.market.repository.MarketCategoryRepository;
import com.example.auction.market.repository.MarketItemDailyLogRepository;
import com.example.auction.market.repository.MarketItemRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// TODO: 오류처리, 테스트, 상수 사용 고려, 로깅 디테일
@Service
@RequiredArgsConstructor
public class MarketApiService {
    private final MarketCategoryRepository marketCategoryRepository;
    private final MarketItemRepository marketItemRepository;
    private final MarketItemDailyLogRepository marketItemDailyLogRepository;
    private final MarketApi marketApi;
    private final ObjectMapper objectMapper;

    @Scheduled(cron = "0 0 14 * * ?")
    public void scheduledTask() {
        System.out.println("TEST");
        insertItemsDailyLog().subscribe();
    }

    public Mono<Void> updateAll() {
        return updateCategories()
                .flatMap(__ -> updateItems())
                .then();
    }

    public Mono<Void> insertItemsDailyLog() {
        List<Integer> categoryIds = Arrays.asList(40000, 50000, 60000, 70000, 90000);
        List<MarketItem> items = marketItemRepository.findByCategory_IdIn(categoryIds);

        Flux<MarketItem> itemFlux = Flux.fromIterable(items);

        return itemFlux
                .concatMap(item -> Mono.just(item)
                        .delayElement(Duration.ofSeconds(1)) // 1초 간격으로 요청
                        .flatMap(this::getItemDailyLog))
                .collectList()
                .doOnNext(logs -> {
                    List<MarketItemDailyLog> flatLogs = logs.stream()
                            .flatMap(List::stream)
                            .collect(Collectors.toList());

                    marketItemDailyLogRepository.saveAll(flatLogs);
                })
                .then();
    }

    private Mono<List<MarketItemDailyLog>> getItemDailyLog(MarketItem item) {
        return marketApi.getItemById(item.getId())
                .map(string -> {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(string).get(0);
                        List<MarketItemDailyLog> dailyLog = parseItemDailyLog(item, jsonNode);
                        // TODO: 예시
                        return dailyLog.subList(1, 2);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        // here
                        return Collections.emptyList();
                    }
                });
    }

    private Mono<List<MarketCategory>> updateCategories() {
        return getCategories()
                .flatMapMany(Flux::fromIterable)
                .flatMap(this::updateTotalCount)
                .collectList()
                .doOnNext(marketCategoryRepository::saveAll);
    }

    private Mono<List<MarketCategory>> getCategories() {
        return marketApi.getOptions()
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

    private Mono<MarketCategory> updateTotalCount(MarketCategory category) {
        return marketApi.postItem(createPostBody(category.getId(), 1))
                .map(string -> {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(string);
                        Integer totalCount = jsonNode.get("TotalCount").asInt();
                        category.updateTotalCount(totalCount);
                        return category;
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull);
    }


    private Mono<Void> updateItems() {
        return Flux.fromIterable(marketCategoryRepository.findAll())
                .flatMap(category -> asyncGetItemsByCategory(category)
                        .doOnNext(items -> marketItemRepository.saveAll(items)))
                .then();
    }

    private Mono<List<MarketItem>> asyncGetItemsByCategory(MarketCategory category) {
        Integer totalCount = category.getTotalCount();

        // TODO: 임시적인 방파제
        if (totalCount > 300) {
            return Mono.just(new ArrayList<>());
        }

        return Flux.range(1, totalCount / 10 + 1)
                .flatMap(pn -> {
                    Map<String, Object> reqBody = createPostBody(category.getId(), pn);
                    return marketApi.postItem(reqBody)
                            .flatMap(string -> {
                                try {
                                    JsonNode jsonNode = objectMapper.readTree(string);
                                    return Mono.just(parseItemsFromCategory(category, jsonNode));
                                } catch (Exception e) {
                                    return Mono.error(e);
                                }
                            });
                })
                .collectList()
                .map(itemsList -> itemsList.stream().flatMap(List::stream).collect(Collectors.toList()));
    }

    private List<MarketItem> parseItemsFromCategory(MarketCategory marketCategory, JsonNode jsonNode) {
        List<MarketItem> items = new ArrayList<>();
        for (JsonNode itemNode : jsonNode.get("Items")) {
            Integer id = itemNode.get("Id").asInt();
            String name = itemNode.get("Name").asText();
            String grade = itemNode.get("Grade").asText();
            String icon = itemNode.get("Icon").asText();

            MarketItem marketItem = new MarketItem(id, name, grade, icon, marketCategory);
            items.add(marketItem);
        }
        return items;
    }

    private List<MarketCategory> parseCategories(JsonNode jsonNode) {
        List<MarketCategory> categories = new ArrayList<>();
        for (JsonNode categoryNode : jsonNode.get("Categories")) {
            Integer code = categoryNode.get("Code").asInt();
            String codeName = categoryNode.get("CodeName").asText();

            MarketCategory marketCategory = new MarketCategory(code, codeName, 0);
            categories.add(marketCategory);
        }

        return categories;
    }

    private List<MarketItemDailyLog> parseItemDailyLog(MarketItem item, JsonNode jsonNode) {
        List<MarketItemDailyLog> logs = new ArrayList<>();
        for (JsonNode logNode : jsonNode.get("Stats")) {
            Date date = Date.valueOf(logNode.get("Date").asText());
            BigDecimal avgPrice = new BigDecimal(logNode.get("AvgPrice").asText());
            Integer tradeCount = logNode.get("TradeCount").asInt();

            MarketItemDailyLog itemDailyLog = new MarketItemDailyLog(item, date, avgPrice, tradeCount);
            logs.add(itemDailyLog);
        }

        return logs;
    }

    private Map<String, Object> createPostBody(Integer categoryCode, Integer pageNo) {
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("Sort", "GRADE");
        bodyMap.put("CategoryCode", categoryCode);
        bodyMap.put("CharacterClass", "");
        bodyMap.put("ItemTier", null);
        bodyMap.put("ItemGrade", "");
        bodyMap.put("ItemName", "");
        bodyMap.put("PageNo", pageNo);
        bodyMap.put("SortCondition", "ASC");
        return bodyMap;
    }
}
