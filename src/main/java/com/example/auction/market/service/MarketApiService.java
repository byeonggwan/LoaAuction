package com.example.auction.market.service;

import com.example.auction.market.api.MarketApi;
import com.example.auction.market.domain.MarketCategory;
import com.example.auction.market.domain.MarketItem;
import com.example.auction.market.repository.MarketCategoryRepository;
import com.example.auction.market.repository.MarketItemRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
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
public class MarketApiService {
    private final MarketCategoryRepository marketCategoryRepository;
    private final MarketItemRepository marketItemRepository;
    private final MarketApi marketApi;
    private final ObjectMapper objectMapper;

    public void saveCategories() {
        marketApi.getOptions().subscribe(string -> {
            try {
                // TODO: string 이 예외일시 처리 필요
                JsonNode jsonNode = objectMapper.readTree(string);
                List<MarketCategory> categories = parseCategories(jsonNode);
                marketCategoryRepository.saveAll(categories);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void updateCategoryTotalCount() {
        List<MarketCategory> categoryList = marketCategoryRepository.findAll();
        for (MarketCategory category : categoryList) {
            marketApi.postItem(createPostBody(category.getId(), 1)).subscribe(string -> {
                try {
                    JsonNode jsonNode = objectMapper.readTree(string);
                    Integer totalCount = jsonNode.get("TotalCount").asInt();
                    category.updateTotalCount(totalCount);
                    marketCategoryRepository.save(category);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void postItems() {
        List<MarketCategory> categoryList = marketCategoryRepository.findAll();
        for (MarketCategory category : categoryList) {
            asyncGetItemsByCategory(category).subscribe(items -> {
                marketItemRepository.saveAll(items);
            });
        }

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
