package com.example.lunchboxkiosk.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisUtilService {

    private final RedisTemplate<String, Object> redisTemplate;

    public String getKey(String pattern) {
        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
        Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().match(pattern).count(100).build());

        while (cursor.hasNext()) {
            return new String(cursor.next());
        }
        return null;
    }

    public Set<String> getKeys(String pattern) {
        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
        Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().match(pattern).count(100).build());

        Set<String> keys = new HashSet<>();  // 반환할 Set을 초기화합니다.

        // Cursor를 통해 모든 키를 가져옵니다.
        while (cursor.hasNext()) {
            keys.add(new String(cursor.next()));  // 키를 Set에 추가
        }

        return keys;  // 모든 키를 포함한 Set 반환
    }
}
