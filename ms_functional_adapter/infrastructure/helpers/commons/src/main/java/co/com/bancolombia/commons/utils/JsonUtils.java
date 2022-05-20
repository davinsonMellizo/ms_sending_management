package co.com.bancolombia.commons.utils;

import co.com.bancolombia.commons.exceptions.TechnicalException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.TECHNICAL_JSON_ERROR;

@UtilityClass
public class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public JsonNode stringToNode(String msg) {
        return stringToType(msg, JsonNode.class);
    }

    public String getNodeAt(JsonNode node, String path) {
        return node.at(path).asText();
    }

    public JsonNode put(JsonNode node, String path, String field, String value) {
        return ((ObjectNode) node.at(path)).put(field, value);
    }

    public JsonNode remove(JsonNode node, String path, String field) {
        return ((ObjectNode) node.at(path)).remove(field);
    }

    public <T> T stringToType(String msg, Class<T> cls) {
        try {
            return OBJECT_MAPPER.readValue(msg, cls);
        } catch (IOException exception) {
            throw new TechnicalException(exception, TECHNICAL_JSON_ERROR);
        }
    }

    public static Map<String, Object> jsonToMap(String json) throws IOException {
        return OBJECT_MAPPER.readValue(json, new TypeReference<HashMap<String, Object>>() {
        });
    }

    public static <T> String toJson(T object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException exception) {
            throw new TechnicalException(exception, TECHNICAL_JSON_ERROR);
        }
    }

    public static <T> T convertValue(Object obj, Class<T> cls) {
        return OBJECT_MAPPER.convertValue(obj, cls);
    }

    public <T> T readValue(String msg, Class<T> cls) {
        try {
            return OBJECT_MAPPER.readValue(msg, cls);
        } catch (JsonProcessingException e) {
            throw new TechnicalException(e, TECHNICAL_JSON_ERROR);
        }
    }

    public Map<String, Object> readValue(String json) {
        try {
            return OBJECT_MAPPER.readValue(json, new TypeReference<HashMap<String, Object>>() {
            });
        } catch (JsonProcessingException exception) {
            throw new TechnicalException(exception, TECHNICAL_JSON_ERROR);
        }
    }
}