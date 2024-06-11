package com.mall.common.autoconfigure.redisson;

import com.mall.common.autoconfigure.redisson.enums.LockType;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

import static com.mall.common.autoconfigure.redisson.enums.LockType.*;

/**
 * LockFactory
 *
 * @author lm
 * @since 2024-06-11 15:36:19
 * @version 1.0
 */
@Component
public class LockFactory {
    private final Map<LockType, Function<String, RLock>> lockHandlers;

    public LockFactory(RedissonClient redissonClient) {
        this.lockHandlers = new EnumMap<>(LockType.class);
        this.lockHandlers.put(RE_ENTRANT_LOCK, redissonClient::getLock);
        this.lockHandlers.put(FAIR_LOCK, redissonClient::getFairLock);
        this.lockHandlers.put(READ_LOCK, name -> redissonClient.getReadWriteLock(name).readLock());
        this.lockHandlers.put(WRITE_LOCK, name -> redissonClient.getReadWriteLock(name).writeLock());
    }

    public RLock getLock(LockType lockType, String name) {
        return lockHandlers.get(lockType).apply(name);
    }
}
