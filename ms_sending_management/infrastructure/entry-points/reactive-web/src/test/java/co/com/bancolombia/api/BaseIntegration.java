package co.com.bancolombia.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.StatusAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.fail;

public class BaseIntegration {


    @Autowired
    protected ApiProperties properties;

    @Autowired
    protected WebTestClient webTestClient;


    @Autowired
    protected ObjectMapper mapper;

    protected <T> T loadFileConfig(String filename, Class<T> clazz) {
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename)) {
            String str = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));
            if (clazz.equals(String.class)) {
                return (T) str;
            }
            return mapper.readValue(str, clazz);
        } catch (IOException e) {
            fail(e.getMessage());
            return null;
        }
    }


    protected StatusAssertions statusAssertionsWebClientPost(String path, String requestBody) {
        return webTestClient.post()
                .uri(path)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(requestBody))
                .exchange()
                .expectStatus();
    }

    protected StatusAssertions statusAssertionsWebClientPut(String path, String requestBody) {
        return webTestClient.put()
                .uri(path)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(requestBody))
                .exchange()
                .expectStatus();
    }

}
