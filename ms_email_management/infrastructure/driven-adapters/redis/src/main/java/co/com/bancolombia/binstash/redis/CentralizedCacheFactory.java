package co.com.bancolombia.binstash.redis;

import co.com.bancolombia.binstash.api.ObjectCache;
import co.com.bancolombia.binstash.redis.adapter.SingleTierObjectCache;
import co.com.bancolombia.binstash.redis.client.RedisStash;
import co.com.bancolombia.binstash.redis.resource.SerializatorHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CentralizedCacheFactory<V extends Object> {

    private final RedisStash centralizedStash;
    private final ObjectMapper objectMapper;

    public ObjectCache<V> newObjectCache() {
        return new SingleTierObjectCache<>(this.centralizedStash,
                new SerializatorHelper<>(objectMapper));
    }
}
