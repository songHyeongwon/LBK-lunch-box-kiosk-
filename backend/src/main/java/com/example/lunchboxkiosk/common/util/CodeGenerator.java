package com.example.lunchboxkiosk.common.util;

import com.example.lunchboxkiosk.common.exception.ErrorCode;
import com.example.lunchboxkiosk.common.exception.InvalidValueException;

import java.util.UUID;
import java.util.regex.Pattern;

public class CodeGenerator {

    private static final Pattern ID_PATTERN = Pattern.compile("^[A-Z]-[a-z0-9]{12}$");

    public static String generateId(String prefix) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String prefixWithHyphen = prefix + "-";
        int remainingLength = 14 - prefixWithHyphen.length();

        if (remainingLength < 0) {
            throw new InvalidValueException(ErrorCode.INVALID_VALUE, prefixWithHyphen);
        }

        return prefixWithHyphen + uuid.substring(0, remainingLength);
    }

    public static void validateIdFormat(String prefix, String id) {
        if (prefix == null || prefix.length() != 1 || !Character.isUpperCase(prefix.charAt(0))) {
            throw new InvalidValueException(ErrorCode.INVALID_VALUE, prefix);
        }

        if (id == null || !ID_PATTERN.matcher(id).matches()) {
            throw new InvalidValueException(ErrorCode.INVALID_VALUE, id);
        }

        if (!id.startsWith(prefix + "-")) {
            throw new InvalidValueException(ErrorCode.INVALID_VALUE, "Key prefix mismatch. Expected prefix: " + prefix);
        }
    }
}
