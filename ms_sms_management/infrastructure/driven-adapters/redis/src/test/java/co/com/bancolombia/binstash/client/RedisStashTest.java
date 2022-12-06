package co.com.bancolombia.binstash.client;

import co.com.bancolombia.binstash.redis.client.RedisStash;
import lombok.extern.java.Log;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Log
class RedisStashTest {

    private static final String TEST_VALUE = "Hello World";
    private static RedisServer redisServer;
    private RedisStash stash;
    private Map<String, String> demoMap;

    @BeforeAll
    static void prepare() throws IOException {
        redisServer = new RedisServer(16379);
        redisServer.start();
    }

    @AfterAll
    static void clean() {
        redisServer.stop();
    }

    @BeforeEach
    void before() {
        demoMap = new HashMap<>();
        demoMap.put("name", "Peter");
        demoMap.put("lastName", "Parker");

        this.stash = new RedisStash.Builder()
                .expireAfter(1)
                .host("127.0.0.1")
                .port(16379)
                .build();

    }


    @Test
    @DisplayName("Should create instance with all props setted")
    void testCreateManual() {
        assertNotNull(new RedisStash.Builder()
                        .dataBaseNumber(0)
                        .expireAfter(1)
                        .host("localhost")
                        .port(16379)
                        //.password("mypwd")
                        .build()
        );
    }

    @Test
    @DisplayName("Should create instance with all props setted")
    void testCreateManual2() {
        assertNotNull(new RedisStash.Builder()
                .dataBaseNumber(0)
                .expireAfter(1)
                //.host("localhost")
                .port(16379)
                //.password("mypwd")
                .build()
        );
    }

    @Test
    @DisplayName("Should create instance with all props setted")
    void testCreateManual3() {
        assertNotNull(new RedisStash.Builder()
                .dataBaseNumber(1)
                .expireAfter(1)
                .port(16379)
                .build()
        );
    }

    @Test
    @DisplayName("Should save element")
    void testPut() {
        StepVerifier.create(stash.save("key1", TEST_VALUE))
                .expectSubscription()
                .expectNext(TEST_VALUE)
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("Should handle save with null key")
    void testPutNullKey() {
        StepVerifier.create(stash.save(null, TEST_VALUE))
                .expectSubscription()
                .expectErrorMessage("Caching key cannot be null")
                .verify();
        StepVerifier.create(stash.save("key1", null))
                .expectSubscription()
                .expectErrorMessage("Caching key cannot be null")
                .verify();
    }

    @Test
    @DisplayName("Should save then get element")
    void testPutGet() {
        Mono<String> op = stash.save("key2", TEST_VALUE)
                .then(stash.get("key2"));

        StepVerifier.create(op)
                .expectSubscription()
                .expectNext(TEST_VALUE)
                .expectComplete()
                .verify();

        StepVerifier.create(stash.get(null))
                .expectSubscription()
                .expectErrorMessage("Caching key cannot be null")
                .verify();
    }

}
