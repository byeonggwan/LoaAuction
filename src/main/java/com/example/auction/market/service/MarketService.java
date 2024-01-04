package com.example.auction.market.service;

import com.example.auction.market.domain.MarketItem;
import com.example.auction.market.domain.MarketItemDailyLog;
import com.example.auction.market.dto.MarketItemDTO;
import com.example.auction.market.dto.MarketItemDailyLogDTO;
import com.example.auction.market.dto.MarketItemDetailDTO;
import com.example.auction.market.repository.MarketCategoryRepository;
import com.example.auction.market.repository.MarketItemDailyLogRepository;
import com.example.auction.market.repository.MarketItemRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MarketService {
    private final MarketCategoryRepository marketCategoryRepository;
    private final MarketItemRepository marketItemRepository;
    private final MarketItemDailyLogRepository marketItemDailyLogRepository;

    public List<MarketItemDTO> getMarketItemSummary() {
        List<MarketItemDTO> items = getItemDTOsOrderByTradeCount();
        return items;
    }

    public MarketItemDetailDTO getMarketItemDetail(Integer id) {
        MarketItem item = marketItemRepository.findById(id).get();
        if (item == null) {
            return null;
        }
        List<MarketItemDailyLogDTO> dailyLogs = getDailyLogDTO(item);
        return new MarketItemDetailDTO(
                item.getId(),
                item.getName(),
                item.getGrade(),
                item.getIcon(),
                dailyLogs
        );
    }

    private List<MarketItemDTO> getItemDTOsOrderByTradeCount() {
        List<MarketItem> items = marketItemRepository.findAll();
        List<MarketItemDTO> itemDTOs = items.stream()
                .map(item -> {
                    MarketItemDailyLogDTO latestLog = getLatestDailyLog(item);
                    if (latestLog == null || latestLog.getAvgPrice() == null
                            || latestLog.getAvgPrice().compareTo(BigDecimal.ZERO) <= 0) {
                        return null;
                    }
                    return new MarketItemDTO(
                            item.getId(),
                            item.getName(),
                            item.getGrade(),
                            item.getIcon(),
                            latestLog.getAvgPrice(),
                            latestLog.getTradeCount()
                    );
                })
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(MarketItemDTO::getTradeCount).reversed())
                .collect(Collectors.toList());
        return itemDTOs;
    }

    private MarketItemDailyLogDTO getLatestDailyLog(MarketItem item) {
        List<MarketItemDailyLogDTO> dailyLogs = getDailyLogDTO(item);

        if (dailyLogs != null && !dailyLogs.isEmpty()) {
            MarketItemDailyLogDTO latestLog = dailyLogs.get(0);
            return latestLog;
        }
        return new MarketItemDailyLogDTO();
    }

    // TODO: log가 너무 많으면 이 방법은 쓸 수 없음 얼마나 자를건지 고려
    private List<MarketItemDailyLogDTO> getDailyLogDTO(MarketItem item){
        List<MarketItemDailyLogDTO> dailyLogs = item.getDailyLogs()
                .stream()
                .map(log -> new MarketItemDailyLogDTO(
                        log.getId().getDate(),
                        log.getAvgPrice(),
                        log.getTradeCount()
                ))
                .collect(Collectors.toList());
        Collections.reverse(dailyLogs);
        return dailyLogs;
    }
}
