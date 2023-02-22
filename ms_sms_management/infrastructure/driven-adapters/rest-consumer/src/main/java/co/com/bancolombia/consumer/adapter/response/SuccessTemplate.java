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
public class SuccessTemplate {

    private Meta meta;
    @JsonProperty("data")
    private Dat data;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder(toBuilder = true)
    public static class Dat{
        private String idTemplate;
        private String messageSubject;
        private String messageBody;
        private String plainText;
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
