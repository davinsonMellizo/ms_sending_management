package co.com.bancolombia.binstash.redis;

import co.com.bancolombia.binstash.api.ObjectCache;
import co.com.bancolombia.binstash.redis.client.RedisStash;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class CentralizedCacheTest {

    @Mock
    private RedisStash redisStash;

    @Mock
    private ObjectMapper objectMapper;

    @Test
    void createCache() {
        ObjectCache<Employee> cache = new CentralizedCacheFactory<Employee>(redisStash, objectMapper).newObjectCache();
        assertNotNull(cache);
    }


}
