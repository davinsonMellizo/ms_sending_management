package co.com.bancolombia.consumer.adapter.mapper;

import lombok.Data;
import org.springframework.util.MultiValueMap;

import java.io.Serializable;
import java.util.Map;

@Data
public class Request implements Serializable {
    private Map<String, String> headers;
    private MultiValueMap<String, String> forms;

    public Request headers(Map<String, String> headers) {
        if (this.getHeaders() != null) {
            this.getHeaders().putAll(headers);
        } else {
            setHeaders(headers);
        }
        return this;
    }
}
