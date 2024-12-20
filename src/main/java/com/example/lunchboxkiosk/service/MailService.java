package com.example.lunchboxkiosk.service;

import com.example.lunchboxkiosk.common.util.CodeGenerator;
import com.example.lunchboxkiosk.model.dto.hsd.HsdMenuSummaryDto;
import com.example.lunchboxkiosk.model.entity.hsd.HsdMenu;
import com.example.lunchboxkiosk.model.entity.hsd.HsdOrder;
import com.example.lunchboxkiosk.model.entity.hsd.HsdOrderMenu;
import com.example.lunchboxkiosk.repository.HsdOrderRepository;
import com.example.lunchboxkiosk.repository.MemoryHsdOrderRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    @Qualifier("memoryHsdOrderRepository")
    private final HsdOrderRepository hsdOrderRepository;

    private static final String HSD_MAIL_FROM = "noreply@accounts.gmail.com";
    private static final String HSD_MAIL_TO = "bumin@digicaps.com";
    private static final String HSD_MAIL_SUBJECT = "[LBK] 주문 목록";

    private String formatPrice(int price) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.KOREA);
        return numberFormat.format(price) + "원";
    }

    private String getHsdEmailContent() {
        createHsdOrdersTest();

        StringBuilder emailContent = new StringBuilder();
        Map<String, List<HsdOrder>> hsdOrders = hsdOrderRepository.findAll();
        Map<String, HsdMenuSummaryDto> menuSummaryMap = new HashMap<>();

        for (List<HsdOrder> orders : hsdOrders.values()) {
            for (HsdOrder order : orders) {
                for (HsdOrderMenu menu : order.getOrderMenus()) {
                    String menuName = menu.getMenu().getName();
                    menuSummaryMap.putIfAbsent(menuName, new HsdMenuSummaryDto(menu.getMenu().getPrice()));
                    HsdMenuSummaryDto summary = menuSummaryMap.get(menuName);
                    summary.addOrder(order.getIp(), menu.getQuantity());
                }
            }
        }

        for (HsdMenuSummaryDto summary : menuSummaryMap.values()) {
            summary.consolidateOrderers();
        }

        emailContent.append("<h1 style='font-family: sans-serif, Arial; color: #333; font-weight: normal; text-align: center;'>주문 목록</h1>");
        emailContent.append("<table border='1' style='border-collapse: collapse; width: 100%; text-align: center; font-family: Arial, sans-serif;'>");
        emailContent.append("<thead style='background-color: #f2f2f2;'>");
        emailContent.append("<tr style='color: #555;'>");
        emailContent.append("<th style='padding: 8px; border: 1px solid #ddd;'>메뉴</th>");
        emailContent.append("<th style='padding: 8px; border: 1px solid #ddd;'>주문자</th>");
        emailContent.append("<th style='padding: 8px; border: 1px solid #ddd;'>수량</th>");
        emailContent.append("<th style='padding: 8px; border: 1px solid #ddd;'>가격</th>");
        emailContent.append("</tr>");
        emailContent.append("</thead>");
        emailContent.append("<tbody>");

        int totalQuantity = 0;
        int totalPrice = 0;
        for (Map.Entry<String, HsdMenuSummaryDto> entry : menuSummaryMap.entrySet()) {
            String menuName = entry.getKey();
            HsdMenuSummaryDto summary = entry.getValue();
            totalQuantity += summary.getTotalQuantity();
            totalPrice += summary.getTotalPrice();

            emailContent.append("<tr style='border: 1px solid #ddd;'>");
            emailContent.append("<td style='padding: 8px; border: 1px solid #ddd; text-align: left;'>").append(menuName).append("</td>");
            emailContent.append("<td style='padding: 8px; border: 1px solid #ddd; text-align: left;'>").append(summary.getOrderers()).append("</td>");
            emailContent.append("<td style='padding: 8px; border: 1px solid #ddd; text-align: right;'>").append(summary.getTotalQuantity()).append("</td>");
            emailContent.append("<td style='padding: 8px; border: 1px solid #ddd; text-align: right;'>").append(formatPrice(summary.getTotalPrice())).append("</td>");
            emailContent.append("</tr>");
        }
        emailContent.append("</tbody>");
        emailContent.append("</table>");
        emailContent.append("<p style='font-family: Arial, sans-serif; text-align: right;'><strong>주문 수량:</strong> ").append(totalQuantity).append("</p>");
        emailContent.append("<p style='font-family: Arial, sans-serif; text-align: right;'><strong>결제 금액:</strong> ").append(formatPrice(totalPrice)).append("</p>");

        return emailContent.toString();
    }

    public void sendHsdOrdersByEmail() {
        String content = getHsdEmailContent();

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(HSD_MAIL_FROM, "Google Accounts <noreply@accounts.gmail.com>");
            helper.setTo(HSD_MAIL_TO);
            helper.setSubject(HSD_MAIL_SUBJECT);
            helper.setText(content,true);
            mailSender.send(message);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void createHsdOrdersTest() {
        hsdOrderRepository.clear();

        // 첫 번째 주문 - 여러 메뉴 포함
        hsdOrderRepository.save(HsdOrder.builder()
                .orderId(CodeGenerator.generateID("O"))
                .ip("192.168.0.1")
                .orderMenus(List.of(
                        new HsdOrderMenu(new HsdMenu("소불고기 청양크림 파스타", 6500, "url1"), 2),
                        new HsdOrderMenu(new HsdMenu("햄버그 나폴리탄 파스타", 5500, "url2"), 1),
                        new HsdOrderMenu(new HsdMenu("소불고기 청양크림 파스타", 6500, "url3"), 3)
                ))
                .build());

        // 두 번째 주문 - 또 다른 메뉴 세트
        hsdOrderRepository.save(HsdOrder.builder()
                .orderId(CodeGenerator.generateID("O"))
                .ip("192.168.0.2")
                .orderMenus(List.of(
                        new HsdOrderMenu(new HsdMenu("오모가리 김치제육덮밥", 4900, "url4"), 1),
                        new HsdOrderMenu(new HsdMenu("햄버그 나폴리탄 파스타", 5500, "url5"), 4)
                ))
                .build());

        // 세 번째 주문 - 단일 메뉴 포함
        hsdOrderRepository.save(HsdOrder.builder()
                .orderId(CodeGenerator.generateID("O"))
                .ip("192.168.0.3")
                .orderMenus(List.of(
                        new HsdOrderMenu(new HsdMenu("햄버그 나폴리탄 파스타", 5500, "url6"), 5)
                ))
                .build());

        // 네 번째 주문 - 동일인이 추가 주문
        hsdOrderRepository.save(HsdOrder.builder()
                .orderId(CodeGenerator.generateID("O"))
                .ip("192.168.0.1")
                .orderMenus(List.of(
                        new HsdOrderMenu(new HsdMenu("오모가리 김치제육덮밥", 4900, "url1"), 1)
                ))
                .build());
    }
}

