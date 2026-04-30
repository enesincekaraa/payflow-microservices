package com.payflow.fraudservice.infrastructure.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class FraudRedisService {

    private final RedisTemplate<String, String> redisTemplate;

    // Bir hesabın belirli zaman penceresindeki işlem sayısını takip et
    public long incrementAndGetTransactionCount(
            String accountId, int windowSeconds) {

        String key = "fraud:txcount:" + accountId;

        Long count = redisTemplate.opsForValue().increment(key);

        // İlk işlemse TTL ayarla — pencere süresi kadar
        if (count != null && count == 1) {
            redisTemplate.expire(key, Duration.ofSeconds(windowSeconds));
        }

        log.debug("Hesap {} için işlem sayısı: {}", accountId, count);
        return count != null ? count : 0;
    }

    // Hesabı fraud olarak işaretle — 24 saat sakla
    public void markAsFraud(String accountId) {
        String key = "fraud:flagged:" + accountId;
        redisTemplate.opsForValue().set(key, "true", Duration.ofHours(24));
    }

    public boolean isFlagged(String accountId) {
        String key = "fraud:flagged:" + accountId;
        return Boolean.TRUE.toString().equals(
                redisTemplate.opsForValue().get(key));
    }
}