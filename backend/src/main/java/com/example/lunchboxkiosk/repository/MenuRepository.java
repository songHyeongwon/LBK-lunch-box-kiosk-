package com.example.lunchboxkiosk.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MenuRepository {

    private final RedisTemplate<String, Object> redisTemplate;

}
