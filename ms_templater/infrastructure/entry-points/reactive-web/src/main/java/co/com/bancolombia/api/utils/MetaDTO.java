package co.com.bancolombia.api.utils;

import co.com.bancolombia.commons.constants.Constants;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.time.LocalDateTime;
import java.util.List;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MetaDTO {

    private static final String DEPLOYMENT_VERSION = "0.0.1";

    @Data
    @Builder(toBuilder = true)
    public static class Meta {

        @JsonProperty("_version")
        private String version;

        @JsonProperty("_requestDate")
        private LocalDateTime requestDate;

        @JsonProperty("_responseSize")
        private int responseSize;

        @JsonProperty("_requestClient")
        private String requestClient;

    }

    public static <T> Meta build(T data, ServerRequest request) {
        return Meta.builder()
                .version(DEPLOYMENT_VERSION)
                .requestDate(LocalDateTime.now())
                .responseSize(getDataSize(data))
                .requestClient(request.path())
                .build();
    }

    private static <T> int getDataSize(T data) {
        if (data instanceof List) {
            return ((List) data).size();
        }
        return Constants.ONE;
    }
}

