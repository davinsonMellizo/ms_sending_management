package co.com.bancolombia.consumer.adapter.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ErrorTemplate extends Throwable{
    private Meta  meta;
    private Error error;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder(toBuilder = true)
    public static class Error{
        private String code;
        private String type;
        private String title;
        private String reason;
        private String detail;
        private String source;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder(toBuilder = true)
    public static class Meta{
        @JsonProperty("_version")
        private String version;
        @JsonProperty("_requestDate")
        private String requestDate;
        @JsonProperty("_responseSize")
        private String responseSize;
        @JsonProperty("_requestClient")
        private String requestClient;
    }

}
