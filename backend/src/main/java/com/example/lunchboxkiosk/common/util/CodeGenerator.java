package com.example.lunchboxkiosk.common.util;

import java.util.UUID;

public class CodeGenerator {

    public static String generateID(String prefix) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String prefixWithHyphen = prefix + "-";
        int remainingLength = 32 - prefixWithHyphen.length();

        if (remainingLength < 0) {
            throw new IllegalArgumentException("Prefix and hyphen combined are too long. Must be less than 32 characters.");
        }

        return prefixWithHyphen + uuid.substring(0, remainingLength);
    }
}
