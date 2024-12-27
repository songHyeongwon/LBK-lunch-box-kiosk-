package com.example.lunchboxkiosk.repository;

import com.example.lunchboxkiosk.model.entity.hsd.HsdOrder;

import java.util.List;
import java.util.Map;

public interface HsdOrderRepository {

    List<HsdOrder> findByIp(String ip);

    Map<String, List<HsdOrder>> findAll();

    void save(HsdOrder hsdOrder);

    void update(String orderId, String ip, HsdOrder updatedOrder);

    void delete(String orderId, String ip);

    void clear();
}
