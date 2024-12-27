package com.example.lunchboxkiosk.repository;

import com.example.lunchboxkiosk.model.entity.hsd.HsdOrder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MemoryHsdOrderRepository implements HsdOrderRepository {

    private static final Map<String, List<HsdOrder>> hsdOrders = new HashMap<>();

    @Override
    public List<HsdOrder> findByIp(String ip) {
        return hsdOrders.getOrDefault(ip, new ArrayList<>());
    }

    @Override
    public Map<String, List<HsdOrder>> findAll() {
        return new HashMap<>(hsdOrders);
    }

    @Override
    public void save(HsdOrder hsdOrder) {
        hsdOrders.putIfAbsent(hsdOrder.getIp(), new ArrayList<>());
        hsdOrders.get(hsdOrder.getIp()).add(hsdOrder);
    }

    @Override
    public void update(String orderId, String ip, HsdOrder updatedOrder) {
        List<HsdOrder> orders = hsdOrders.get(ip);
        if (orders == null) {
            throw new RuntimeException("No orders found for ip: " + ip);
        }

        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getOrderId().equals(orderId)) {
                orders.set(i, updatedOrder);
                return;
            }
        }
        throw new RuntimeException("Order not found for ip: " + ip + " and order id: " + orderId);
    }

    @Override
    public void delete(String orderId, String ip) {
        List<HsdOrder> orders = hsdOrders.get(ip);
        if (orders == null) {
            throw new RuntimeException("No orders found for ip: " + ip);
        }

        boolean removed = orders.removeIf(order -> order.getOrderId().equals(orderId));
        if (!removed) {
            throw new RuntimeException("Order not found for ip: " + ip + " and order id: " + orderId);
        }
    }

    @Override
    public void clear() {
        hsdOrders.clear();
    }
}
