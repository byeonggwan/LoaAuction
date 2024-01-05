package com.example.auction;

import org.springframework.stereotype.Component;

@Component
public class ThymeleafUtil {
    public String translateGrade(String grade) {
        switch (grade) {
            case "일반":
                return "common";
            case "고급":
                return "uncommon";
            case "희귀":
                return "rare";
            case "영웅":
                return "epic";
            case "전설":
                return "legendary";
            case "유물":
                return "mythic";
            case "에스더":
                return "esther";
            default:
                return "";
        }
    }
}
