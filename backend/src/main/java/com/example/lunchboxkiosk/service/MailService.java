package com.example.lunchboxkiosk.service;

import com.example.lunchboxkiosk.model.dto.common.*;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final OrderService orderService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final String MAIL_FROM = "noreply@accounts.gmail.com";
    private static final String MAIL_TO = "bumin@digicaps.com";
    private static final String MAIL_SUBJECT = "주문 목록";

    private String formatPrice(AtomicInteger price) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.KOREA);
        return numberFormat.format(price) + "원";
    }

    private String getEmailContent() {
        String keyPattern = LocalDateTime.now().format(DATE_FORMATTER) + ":*";
        Set<String> keys = orderService.makeOrderKeys(keyPattern);
        List<OrderDto> orderDtos = orderService.getOrdersByKey(keys);
        List<OrderDetailDto> orderDetailDtos = orderService.getMenuDetailsByOrderMenu(orderDtos);

        StringBuilder content = new StringBuilder();

        // HTML 테이블 시작
        content.append("<h3 style='font-family: sans-serif, Arial; color: #333; text-align: center;'>주문 목록</h1>");
        content.append("<table border='1' style='border-collapse: collapse; width: 100%; text-align: center; font-family: Arial, sans-serif;'>");

        // 테이블 헤더
        content.append("<thead style='background-color: #f2f2f2;'>");
        content.append("<tr style='color: #555;'>");
        content.append("<th style='padding: 8px; border: 1px solid #ddd;'>주문자</th>");
        content.append("<th style='padding: 8px; border: 1px solid #ddd;'>주문 메뉴</th>");
        content.append("<th style='padding: 8px; border: 1px solid #ddd;'>수량</th>");
        content.append("<th style='padding: 8px; border: 1px solid #ddd;'>총 가격</th>");
        content.append("</tr>");
        content.append("</thead>");
        content.append("<tbody>");

        // 주문 데이터 삽입
        AtomicInteger dateTotalQuantity = new AtomicInteger();
        AtomicInteger dateTotalPrice = new AtomicInteger();
        orderDetailDtos.forEach(orderDetail -> {
            OrderDto order = orderDetail.getOrder();
            List<MenuDetailDto> menus = orderDetail.getMenus();

            String email = order.getEmail();
            StringBuilder menuNames = new StringBuilder();
            int totalQuantity = 0;
            int totalPrice = order.getTotalPrice();

            for (MenuDetailDto menuDetail : menus) {
                MenuDto menu = menuDetail.getMenu();
                OrderMenuDto orderMenu = order.getMenus().stream()
                        .filter(m -> m.getId().equals(menu.getId()))
                        .findFirst()
                        .orElse(null);

                if (orderMenu != null) {
                    totalQuantity += orderMenu.getQuantity();
                    menuNames.append(menu.getName())
                            .append(" (")
                            .append(orderMenu.getQuantity())
                            .append("개)<br>");
                }
            }

            dateTotalQuantity.addAndGet(totalQuantity);
            dateTotalPrice.addAndGet(totalPrice);

            content.append("<tr>");
            content.append("<td style='padding: 8px; border: 1px solid #ddd;'>").append(email).append("</td>");
            content.append("<td style='padding: 8px; border: 1px solid #ddd;'>").append(menuNames).append("</td>");
            content.append("<td style='padding: 8px; border: 1px solid #ddd;'>").append(totalQuantity).append("</td>");
            content.append("<td style='padding: 8px; border: 1px solid #ddd;'>").append(totalPrice).append("</td>");
            content.append("</tr>");
        });

        content.append("</tbody>");
        content.append("</table>");

        content.append("<p style='font-family: Arial, sans-serif; text-align: right;'><strong>금일 총 주문 수량:</strong> ").append(dateTotalQuantity).append("</p>");
        content.append("<p style='font-family: Arial, sans-serif; text-align: right;'><strong>금일 총 결제 금액:</strong> ").append(formatPrice(dateTotalPrice)).append("</p>");

        return content.toString();
    }

    public void sendDateOrderInfoEmail() {
        String content = getEmailContent();

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(MAIL_FROM, "Google Accounts <noreply@accounts.gmail.com>");
            helper.setTo(MAIL_TO);
            helper.setSubject(MAIL_SUBJECT);
            helper.setText(content,true);
            mailSender.send(message);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}

