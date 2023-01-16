package co.com.bancolombia.events.commons;

import co.com.bancolombia.Request;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.reactivecommons.async.impl.communications.Message;
import org.reactivecommons.async.impl.exceptions.MessageConversionException;
import org.springframework.beans.factory.annotation.Value;
import reactor.core.publisher.Mono;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public abstract class Converter {

    @Value("${app.async.maxRetries}")
    private String maxRetries;
    private final ObjectMapper objectMapper;

    protected <T extends Request> Mono<T> converterMessage(Message message, Class<T> valueClass) {
        try {
            final CommandJson commandJson = objectMapper.readValue(message.getBody(), CommandJson.class);
            final T value = objectMapper.treeToValue(commandJson.getData(), valueClass);
            Map<String, String> headers = (Map) addHeader(message.getProperties().getHeaders(), "retryNumber",
                    maxRetries);
            value.setHeaders(headers);
            return Mono.just(value);
        } catch (IOException e) {
            throw new MessageConversionException("Failed to convert Message content", e);
        }
    }

    private Map<String, Object> addHeader(Map<String, Object> headers, String key, String value) {
        if (headers == null || headers.isEmpty()) {
            headers = new HashMap<>();
        }
        headers.put(key, value);
        return headers;
    }

    @Data
    private static class CommandJson {
        private String name;
        private String commandId;
        private JsonNode data;
    }
}
