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

    private SingleTierObjectCache<Token> cache;//puse esto modifique Token

    @Mock
    private RedisStash mockedStash;

    private ObjectMapper objectMapper;

    private SerializatorHelper<Token> serializatorHelper;

    private Token token;

    private String serializedPerson;

    @BeforeEach
    void before() {
        objectMapper = new ObjectMapper();

        token = new Token();
        token.setAccessToken("AccessToken");
        token.setExpiresIn(1234L);
        token.setRefreshToken("Refresh");
        token.setTokenType("tokenType");

        serializedPerson = "";
        try {
            serializedPerson = this.objectMapper.writeValueAsString(token);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

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
/*
    @Test
    @DisplayName("get from cache (List object)")
    void testGetList() {
        SerializatorHelper<List<Person>> serializatorHelper2 = new SerializatorHelper<>(objectMapper);

        SingleTierObjectCacheUseCase<List<Person>> cache2 =
                new SingleTierObjectCacheUseCase<>(mockedStash, serializatorHelper2);

        String serializedListOfPerson = "";
        try {
            serializedListOfPerson = this.objectMapper.writeValueAsString(List.of(p));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        when(mockedStash.save(anyString(), anyString())).thenReturn(Mono.just(serializedListOfPerson));
        when(mockedStash.get(anyString())).thenReturn(Mono.just(serializedListOfPerson));

        Mono<List<Person>> persons = cache2.save("persons", List.of(p))
                .then(cache2.get("persons", new TypeReference<List<Person>>() {}));

        StepVerifier.create(persons)
                .expectSubscription()
                .expectNext(List.of(p))
                .expectComplete()
                .verify();

        verify(mockedStash).save("persons", serializedListOfPerson);
        verify(mockedStash).get("persons");
    }

    @Test
    @DisplayName("get from cache (Map object)")
    void testGetMap() {
        SerializatorHelper<Map<String, Person>> serializatorHelper2 = new SerializatorHelper<>(objectMapper);

        SingleTierObjectCacheUseCase<Map<String, Person>> cache2 =
                new SingleTierObjectCacheUseCase<>(mockedStash, serializatorHelper2);

        String serializedMapOfPerson = "";
        try {
            serializedMapOfPerson = this.objectMapper.writeValueAsString(Map.of("p1", p));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        when(mockedStash.save(anyString(), anyString())).thenReturn(Mono.just(serializedMapOfPerson));
        when(mockedStash.get(anyString())).thenReturn(Mono.just(serializedMapOfPerson));

        Mono<Map<String, Person>> persons = cache2.save("persons", Map.of("p1", p))
                .then(cache2.get("persons", new TypeReference<Map<String, Person>>() {}))
                .log();

        StepVerifier.create(persons)
                .expectSubscription()
                .expectNext(Map.of("p1", p))
                .expectComplete()
                .verify();

        verify(mockedStash).save("persons", serializedMapOfPerson);
        verify(mockedStash).get("persons");
    }

    @Test
    @DisplayName("Check element exists on cache")
    void testExist() {

        when(mockedStash.exists(anyString())).thenReturn(Mono.just(true));

        StepVerifier.create(cache.exists("pparker"))
                .expectSubscription()
                .expectNext(true)
                .expectComplete()
                .verify();

        verify(mockedStash).exists("pparker");
    }

    @Test
    @DisplayName("Get keyset")
    void testGetKeyset() {

        when(mockedStash.keySet()).thenReturn(Mono.just(Set.of("pparker")));

        StepVerifier.create(cache.keySet())
                .expectSubscription()
                .expectNext(Set.of("pparker"))
                .expectComplete()
                .verify();

        verify(mockedStash).keySet();
    }

    @Test
    @DisplayName("evict key in cache")
    void testEvict() {

        when(mockedStash.evict(anyString())).thenReturn(Mono.just(true));

        Mono<Boolean> personMono = cache.evict("pparker");

        StepVerifier.create(personMono)
                .expectSubscription()
                .expectNext(true)
                .expectComplete()
                .verify();

        verify(mockedStash).evict("pparker");
    }
*/
}
