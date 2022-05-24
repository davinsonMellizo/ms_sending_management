package co.com.bancolombia.binstash.redis.adapter;

import co.com.bancolombia.binstash.api.ObjectCache;
import co.com.bancolombia.binstash.redis.client.RedisStash;
import co.com.bancolombia.binstash.redis.resource.SerializatorHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;

@Log
@RequiredArgsConstructor
public class SingleTierObjectCache<T> implements ObjectCache<T> {

    private final RedisStash cache;
    private final SerializatorHelper<T> serializatorHelper;

    @Override
    public Mono<T> save(String key, T value) {
        return Mono.just(value)
                .map(this::serialize)
                .flatMap(serialized -> cache.save(key, serialized))
                .map(r -> value);
    }

    @Override
    public Mono<T> get(String key, Class<T> clazz) {
        return Mono.just(key)
                .flatMap(cache::get)
                .map(serialized -> this.deserialize(serialized, clazz));
    }

    private String serialize(T obj) {
        return serializatorHelper.serialize(obj);
    }

    private T deserialize(String obj, Class<T> clazz) {
        return this.serializatorHelper.deserializeTo(obj, clazz);
    }

}
