package com.example.lunchboxkiosk.repository;

import com.example.lunchboxkiosk.common.util.CodeGenerator;
import com.example.lunchboxkiosk.model.entity.hsd.HsdMenu;
import com.example.lunchboxkiosk.model.entity.hsd.HsdOrder;
import com.example.lunchboxkiosk.model.entity.hsd.HsdOrderMenu;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class MemoryHsdOrderRepositoryTest {

    private HsdOrderRepository repository;

    @BeforeEach
    void beforeEach() {
        repository = new MemoryHsdOrderRepository();
    }

    @AfterEach
    void afterEach() {
        repository.clear();
    }

    private HsdOrder createOrder(String ip, HsdOrderMenu... orderMenus) {
        return HsdOrder.builder()
                .orderId(CodeGenerator.generateID("O")) // 주문 ID 생성
                .ip(ip)
                .orderMenus(List.of(orderMenus)) // 주문 메뉴 리스트 추가
                .build();
    }

    private HsdOrderMenu createOrderMenu(String name, int price, String imageUrl, int quantity) {
        HsdMenu menu = HsdMenu.builder()
                .name(name)
                .price(price)
                .imageUrl(imageUrl)
                .build();

        return HsdOrderMenu.builder()
                .quantity(quantity)
                .menu(menu)
                .build();
    }

    @Test
    @DisplayName("주문 저장 테스트")
    void save() {
        // Arrange: 데이터 준비
        HsdOrder order = createOrder("192.168.0.1",
                createOrderMenu("메뉴1", 1000, "url1", 1),
                createOrderMenu("메뉴2", 1000, "url2", 2));

        // Act: 테스트 대상 메서드 실행
        repository.save(order);

        // Assert: 결과 검증
        List<HsdOrder> savedOrders = repository.findByIp("192.168.0.1");
        assertThat(savedOrders)
                .hasSize(1)
                .containsExactly(order);
    }

    @Test
    @DisplayName("특정 IP의 주문 조회")
    void findByIp_shouldReturnOrdersForGivenIp() {
        // Arrange: 데이터 준비
        HsdOrder order1 = createOrder("192.168.0.1", createOrderMenu("메뉴1", 1000, "url1", 1));
        HsdOrder order2 = createOrder("192.168.0.1", createOrderMenu("메뉴2", 2000, "url2", 2));
        repository.save(order1);
        repository.save(order2);

        // Act: 특정 IP로 조회
        List<HsdOrder> orders = repository.findByIp("192.168.0.1");

        // Assert: 결과 검증
        assertThat(orders).hasSize(2);
        assertThat(orders).containsExactly(order1, order2);
    }

    @Test
    @DisplayName("존재하지 않는 IP 조회")
    void findByIp_shouldReturnEmptyListForNonExistentIp() {
        // Act: 존재하지 않는 IP 조회
        List<HsdOrder> orders = repository.findByIp("192.168.0.2");

        // Assert: 빈 리스트 반환 검증
        assertThat(orders).isEmpty();
    }

    @Test
    @DisplayName("모든 주문 조회")
    void findAll_shouldReturnAllOrdersGroupedByIp() {
        // Arrange: 데이터 준비
        HsdOrder order1 = createOrder("192.168.0.1", createOrderMenu("메뉴1", 1000, "url1", 1));
        HsdOrder order2 = createOrder("192.168.0.2", createOrderMenu("메뉴2", 2000, "url2", 2));
        repository.save(order1);
        repository.save(order2);

        // Act: 모든 주문 조회
        Map<String, List<HsdOrder>> allOrders = repository.findAll();

        // Assert: 결과 검증
        assertThat(allOrders).hasSize(2);
        assertThat(allOrders.get("192.168.0.1")).containsExactly(order1);
        assertThat(allOrders.get("192.168.0.2")).containsExactly(order2);
    }

    @Test
    @DisplayName("주문이 없을 때 모든 주문 조회")
    void findAll_shouldReturnEmptyMapIfNoOrders() {
        // Act: 주문 데이터가 없는 상태에서 모든 주문 조회
        Map<String, List<HsdOrder>> allOrders = repository.findAll();

        // Assert: 빈 맵 반환 검증
        assertThat(allOrders).isEmpty();
    }

    @Test
    @DisplayName("주문 업데이트 테스트")
    void update_shouldUpdateOrderSuccessfully() {
        // Arrange
        HsdOrder originalOrder = createOrder("192.168.0.1", createOrderMenu("메뉴1", 1000, "url1", 1));
        repository.save(originalOrder);

        HsdOrder updatedOrder = createOrder("192.168.0.1", createOrderMenu("메뉴1", 2000, "url1", 2));

        // Act
        repository.update(originalOrder.getOrderId(), originalOrder.getIp(), updatedOrder);

        // Assert
        List<HsdOrder> orders = repository.findByIp("192.168.0.1");
        assertThat(orders).hasSize(1);
        assertThat(orders.get(0)).isEqualTo(updatedOrder);
    }

    @Test
    @DisplayName("주문 삭제 테스트 - 성공")
    void delete_shouldDeleteOrderSuccessfully() {
        // Arrange
        HsdOrder order = createOrder("192.168.0.1", createOrderMenu("메뉴1", 1000, "url1", 1));
        repository.save(order);

        // Act
        repository.delete(order.getOrderId(), order.getIp());

        // Assert
        List<HsdOrder> orders = repository.findByIp("192.168.0.1");
        assertThat(orders).isEmpty();
    }
}
