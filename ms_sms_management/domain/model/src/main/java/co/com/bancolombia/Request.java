package co.com.bancolombia;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class Request implements Serializable {
    private Map<String, Object> headers;

    public Request headers(Map<String, Object> headers) {
        if (this.getHeaders() != null) {
            this.getHeaders().putAll(headers);
        } else {
            setHeaders(headers);
        }
        return this;
    }
}
