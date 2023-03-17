package co.com.bancolombia.binstash.redis.config;

import co.com.bancolombia.binstash.api.ObjectCache;
import co.com.bancolombia.binstash.redis.CentralizedCacheFactory;
import co.com.bancolombia.binstash.redis.client.RedisStash;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Configuration
public class CentralizedCacheConfig<V extends Object> {

    @Value("${stash.redis.host}")
    private String host;

    @Value("${stash.redis.port}")
    private int port;

    @Value("${stash.redis.database:0}")
    private int database;

    @Value("${stash.redis.password:}")
    private String password;

    @Value("${stash.redis.expireTime:}")
    private int redisExpireTime;

    @Bean(name = "centralMemStashBean")
    public RedisStash redisStash() {
        return new RedisStash.Builder()
                .expireAfter(redisExpireTime)
                .host(host)
                .port(port)
                .dataBaseNumber(database)
                .password(password)
                .build();
    }

    @Bean
    public CentralizedCacheFactory<V> newFactory(@Qualifier("centralMemStashBean") RedisStash centralizedStash,
                                                 ObjectMapper objectMapper) {
        return new CentralizedCacheFactory<>(centralizedStash, objectMapper);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public ObjectCache<ArrayList<String>> tokenCacheInalambria(CentralizedCacheFactory<ArrayList<String>> centralizedCacheFactory) {
        return centralizedCacheFactory.newObjectCache();
    }

}
