package co.com.bancolombia.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
@Builder(toBuilder = true)
public class Request implements Serializable {
    private Map<String, String> headers;

    public Request headers(Map<String, String> headers) {
        if (this.getHeaders() != null) {
            this.getHeaders().putAll(headers);
        } else {
            setHeaders(headers);
        }
        return this;
    }
}
