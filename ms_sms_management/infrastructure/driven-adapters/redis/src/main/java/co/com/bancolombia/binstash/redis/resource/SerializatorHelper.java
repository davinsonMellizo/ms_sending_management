package co.com.bancolombia.binstash.redis.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@RequiredArgsConstructor
public class SerializatorHelper<T> {

    private final ObjectMapper objectMapper;

    public String serialize(T obj) {
        try {
            if (obj == null) {
                return null;
            }
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.severe(e.getMessage());
            return null;
        }
    }

    public T deserializeTo(String obj, Class<T> clazz) {
        try {
            if (obj == null || clazz == null) {
                return null;
            }
            return objectMapper.readValue(obj, clazz);
        } catch (Exception e) {
            log.severe(e.getMessage());
            return null;
        }
    }

}
