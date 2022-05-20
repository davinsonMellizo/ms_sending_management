package co.com.bancolombia.commons.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JsonUtilsTest {

    private static final String msj = "{\"testFiled\":\"test\"}";
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String jsonString = "{\"testClass\":{\"testFiled\":\"123\"}}";
    private static JsonNode jsonNode;
    private static final String path = "/testClass/testFiled";

    @BeforeEach
    public void init() throws IOException {
        jsonNode = mapper.readTree(jsonString);
    }

    @Test
    void stringToNodeTest() {
        assertThat(JsonUtils.stringToNode(msj)).isNotNull();
    }

    @Test
    void getNodeAtTest() {
        assertEquals("123", JsonUtils.getNodeAt(jsonNode, path));
    }

    @Test
    void putTest() {
        assertThat(JsonUtils.put(jsonNode, "/testClass", "test2", "test2")).isNotNull();
    }

    @Test
    void removeTest() {
        assertThat(JsonUtils.remove(jsonNode, "/testClass", "testFiled")).isNotNull();
    }

    @Test
    void stringToTypeTest() {
        assertThat(JsonUtils.stringToType(msj, TestClass.class)).isNotNull();
    }

    @Test
    void jsonToMapTest() throws IOException {
        assertThat(JsonUtils.jsonToMap(msj)).isNotNull();
    }

    @Test
    void toJsonTest() throws IOException {
        assertThat(JsonUtils.toJson(jsonString)).isNotNull();
    }

    @Test
    void stringToTypeFail() {
        assertThrows(Exception.class, () -> JsonUtils.stringToType("Hola", Integer.class));
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    static class TestClass {
        private String testFiled;
    }

}
