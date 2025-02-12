package com.example.lunchboxkiosk.repository;

import com.example.lunchboxkiosk.model.dto.common.MenuDto;
import com.example.lunchboxkiosk.model.entity.Menu;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MenuRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;

    public void saveMenus(String brandId, List<MenuDto> menuDtoList) {
        menuDtoList.forEach(menuDto -> {
            Menu menu = modelMapper.map(menuDto, Menu.class);
            String key = "menu:" + brandId + ":" + menu.getCategoryId() + ":" + menu.getId();

            redisTemplate.opsForValue().set(key, menu, 1, TimeUnit.DAYS);

            redisTemplate.opsForZSet().add("search:brand:all:category:all:menu:all", menu.getId(), System.currentTimeMillis());
            redisTemplate.opsForZSet().add("search:brand:" + brandId + ":category:all:menu:all", menu.getId(), System.currentTimeMillis());
            redisTemplate.opsForZSet().add("search:brand:" + brandId + ":category:" + menu.getCategoryId() + ":menu:all", menu.getId(), System.currentTimeMillis());

            redisTemplate.expire("search:brand:all:category:all:menu:all", 1, TimeUnit.DAYS);
            redisTemplate.expire("search:brand:" + brandId + ":category:all:menu:all", 1, TimeUnit.DAYS);
            redisTemplate.expire("search:brand:" + brandId + ":category:" + menu.getCategoryId() + ":menu:all", 1, TimeUnit.DAYS);
        });
    }

    public MenuDto findById(String key) {
        Object menuData = redisTemplate.opsForValue().get(key);
        return objectMapper.convertValue(menuData, MenuDto.class);
    }

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
