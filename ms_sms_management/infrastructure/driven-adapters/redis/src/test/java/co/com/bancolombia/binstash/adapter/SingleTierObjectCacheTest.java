package co.com.bancolombia.binstash.adapter;


import co.com.bancolombia.binstash.redis.adapter.SingleTierObjectCache;
import co.com.bancolombia.binstash.redis.client.RedisStash;
import co.com.bancolombia.binstash.redis.resource.SerializatorHelper;
import co.com.bancolombia.model.token.Token;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SingleTierObjectCacheTest {

    private SingleTierObjectCache<Token> cache;

    @Mock
    private RedisStash mockedStash;

    private ObjectMapper objectMapper;

    private SerializatorHelper<Token> serializatorHelper;

    private Token p;

    private String serializedPerson;

    @BeforeEach
    void before() throws JsonProcessingException {
        objectMapper = new ObjectMapper();

        p = new Token();
        p.setAccessToken("AccessToken");
        p.setExpiresIn(1234L);
        p.setRefreshToken("Refresh");
        p.setTokenType("tokenType");

        serializedPerson = "";
        serializedPerson = this.objectMapper.writeValueAsString(p);

        serializatorHelper = new SerializatorHelper<>(objectMapper);

        cache = new SingleTierObjectCache<>(mockedStash, serializatorHelper);
    }

    @Test
    @DisplayName("Create cache")
    void testCreate() {
        assertNotNull(cache);
    }

    @Test
    @DisplayName("save in cache")
    void testSave() {
        assert cache != null;

        when(mockedStash.save(anyString(), anyString())).thenReturn(Mono.just(serializedPerson));

        StepVerifier.create(cache.save("pparker", p))
                .expectSubscription()
                .expectNext(p)
                .expectComplete()
                .verify();

        verify(mockedStash).save("pparker", serializedPerson);
    }

    @Test
    @DisplayName("Get from cache")
    void testGet() {
        assert cache != null;

        when(mockedStash.save(anyString(), anyString())).thenReturn(Mono.just(serializedPerson));
        when(mockedStash.get(anyString())).thenReturn(Mono.just(serializedPerson));

        Mono<Token> personMono = cache.save("pparker", p)
                .then(cache.get("pparker", Token.class));//puse esto, le puse token

        StepVerifier.create(personMono)
                .expectSubscription()
                .expectNextMatches(received -> {
                    assert received.equals(p);
                    return true;
                })
                .expectComplete()
                .verify();

        verify(mockedStash).save("pparker", serializedPerson);
        verify(mockedStash).get("pparker");
    }

}
