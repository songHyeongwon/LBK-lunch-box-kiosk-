package com.example.lunchboxkiosk.model.dto.hsd;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class HsdMenuSummaryDto {
    private final int price;
    private int totalQuantity;
    private final Map<String, Integer> ordererMap;

    public HsdMenuSummaryDto(int price) {
        this.price = price;
        this.totalQuantity = 0;
        this.ordererMap = new HashMap<>();
    }

    public void addOrder(String ip, int quantity) {
        ordererMap.put(ip, ordererMap.getOrDefault(ip, 0) + quantity);
        totalQuantity += quantity;
    }

    public void consolidateOrderers() {
        StringBuilder consolidatedOrderers = new StringBuilder();
        for (Map.Entry<String, Integer> entry : ordererMap.entrySet()) {
            if (consolidatedOrderers.length() > 0) {
                consolidatedOrderers.append(", ");
            }
            consolidatedOrderers.append(entry.getKey()).append("(").append(entry.getValue()).append(")");
        }
        orderers = consolidatedOrderers.toString();
    }

    private String orderers;

    public int getTotalPrice() {
        return totalQuantity * price;
    }
}
