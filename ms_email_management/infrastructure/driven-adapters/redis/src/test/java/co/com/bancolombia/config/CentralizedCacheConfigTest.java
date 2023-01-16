package co.com.bancolombia.config;

import co.com.bancolombia.binstash.redis.config.CentralizedCacheConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import redis.embedded.RedisServer;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CentralizedCacheConfigTest {

    private static RedisServer redisServer;
    private CentralizedCacheConfig config;

    @BeforeAll
    static void prepare() throws IOException {
        redisServer = new RedisServer(16377);
        redisServer.start();
    }

    @AfterAll
    static void clean() {
        redisServer.stop();
    }

    @BeforeEach
    void before() {
        config = new CentralizedCacheConfig();
        ReflectionTestUtils.setField(config, "host", "localhost");
        ReflectionTestUtils.setField(config, "port", 16377);
    }

    @Test
    @DisplayName("Create redis stash")
    void createStash() {
        assertNotNull(config.redisStash());
    }

    @Test
    @DisplayName("Create factory")
    void createFactory() {
        assertNotNull(config.newFactory(config.redisStash(), new ObjectMapper()));
    }
}
