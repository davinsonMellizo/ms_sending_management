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

    private Token token;

    private String serializedPerson;

    @BeforeEach
    void before() throws JsonProcessingException {
        objectMapper = new ObjectMapper();

        token = new Token();
        token.setAccessToken("AccessToken");
        token.setExpiresIn(1234L);
        token.setRefreshToken("Refresh");
        token.setTokenType("tokenType");

        serializedPerson = "";
        serializedPerson = this.objectMapper.writeValueAsString(token);
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

        StepVerifier.create(cache.save("pparker", token))
                .expectSubscription()
                .expectNext(token)
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

        Mono<Token> personMono = cache.save("pparker", token)
                .then(cache.get("pparker", Token.class));//puse esto, le puse token

        StepVerifier.create(personMono)
                .expectSubscription()
                .expectNextMatches(received -> {
                    assert received.equals(token);
                    return true;
                })
                .expectComplete()
                .verify();

        verify(mockedStash).save("pparker", serializedPerson);
        verify(mockedStash).get("pparker");
    }

}
