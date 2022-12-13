package co.com.bancolombia.consumer.adapter.response.model;

import lombok.Data;
import org.springframework.util.MultiValueMap;

import java.io.Serializable;
import java.util.Map;

@Data
public class RequestForm implements Serializable {
    private Map<String, String> headers;
    private MultiValueMap<String, String> forms;

    public RequestForm headers(Map<String, String> headers) {
        if (this.getHeaders() != null) {
            this.getHeaders().putAll(headers);
        } else {
            setHeaders(headers);
        }
        return this;
    }
}
