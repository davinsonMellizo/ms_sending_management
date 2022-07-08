package co.com.bancolombia.binstash.resource;



import co.com.bancolombia.binstash.redis.resource.SerializatorHelper;
import co.com.bancolombia.model.token.Token;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SerializatorHelperTest {

    @Mock
    ObjectMapper objectMapper;

    @SneakyThrows
    @Test
    @DisplayName("Should handle error on write json")
    void testRaiseError() {
        JsonProcessingException e = new JsonProcessingException("Dummy Error") {
        };
        doThrow(e).when(objectMapper).writeValueAsString(any());
        SerializatorHelper<Token> sHelper = new SerializatorHelper<>(objectMapper);
        String s = sHelper.serialize(new Token());
        assertNull(s);
        verify(objectMapper).writeValueAsString(any(Token.class));
    }

    @SneakyThrows
    @Test
    @DisplayName("Should handle error on parse json")
    void testRaiseError2() {
        JsonProcessingException e = new JsonProcessingException("Dummy Error") {
        };
        doThrow(e).when(objectMapper).readValue(anyString(), any(Class.class));
        SerializatorHelper<Token> sHelper = new SerializatorHelper<>(objectMapper);
        sHelper.deserializeTo("{}", Token.class);
        verify(objectMapper).readValue(anyString(), any(Class.class));
    }

    @SneakyThrows
    @Test
    @DisplayName("Should handle null args")
    void testHandleNullArgs() {
        SerializatorHelper<Token> sHelper = new SerializatorHelper<>(objectMapper);

        assertNull(sHelper.serialize(null));
        assertNull(sHelper.deserializeTo(null, Token.class));
        assertNull(sHelper.deserializeTo("pparker", null));

        verify(objectMapper, times(0)).writeValueAsString(any(Token.class));
        verify(objectMapper, times(0)).readValue(anyString(), any(Class.class));
        verify(objectMapper, times(0)).readValue(anyString(), any(TypeReference.class));
    }
}
