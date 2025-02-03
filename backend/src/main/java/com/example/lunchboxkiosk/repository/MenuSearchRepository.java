package com.example.lunchboxkiosk.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MenuSearchRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    public Long countAllByKey(String key) {
        Long totalElements = redisTemplate.opsForZSet().zCard(key);
        return (totalElements == null) ? 0L : totalElements;
    }

    public Set<Object> findAllWithPaging(String key, int startIndex, int endIndex) {
        return redisTemplate.opsForZSet().range(key, startIndex, endIndex);
    }

    public Set<Object> findByBrandIdWithPaging(String key, int startIndex, int endIndex) {
        return redisTemplate.opsForZSet().range(key, startIndex, endIndex);
    }

    public Set<Object> findByBrandIdAndCategoryIdWithPaging(String key, int startIndex, int endIndex) {
        return redisTemplate.opsForZSet().range(key, startIndex, endIndex);
    }
}
