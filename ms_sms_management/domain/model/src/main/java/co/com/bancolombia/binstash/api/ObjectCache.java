package co.com.bancolombia.binstash.api;

import reactor.core.publisher.Mono;

/**
 * Cache for storing &lt;String, T&gt; data.
 */
public interface ObjectCache<T> {

    /**
     * Save value to cache
     * @param key key to index value
     * @param value value to store
     * @return value stored
     */
    Mono<T> save(String key, T value);

    /**
     * Gets an element from cache
     * @param key key to which value was stored
     * @param clazz The class type of object stored for deserialization purposes
     * @return value stored under key or empty if no such key exists in cache.
     */
    Mono<T> get(String key, Class<T> clazz);

}

