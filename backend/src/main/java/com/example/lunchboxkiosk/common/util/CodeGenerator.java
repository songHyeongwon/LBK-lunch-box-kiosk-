package com.example.lunchboxkiosk.common.util;

import java.util.UUID;

public class CodeGenerator {

    public static String generateID(String prefix) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String prefixWithHyphen = prefix + "-";
        int remainingLength = 32 - prefixWithHyphen.length();

        // 남은 길이가 UUID 길이를 초과하지 않도록 검사
        if (remainingLength < 0) {
            throw new IllegalArgumentException("Prefix and hyphen combined are too long. Must be less than 32 characters.");
        }

        // UUID에서 필요한 길이만큼 잘라내어 prefixWithHyphen과 결합
        return prefixWithHyphen + uuid.substring(0, remainingLength);
    }
}
