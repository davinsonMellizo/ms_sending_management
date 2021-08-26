package co.com.bancolombia;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class RequestHeaders implements Serializable {
    private Map<String,String> headers;

    public RequestHeaders headers(Map<String,String> headers) {
        if (this.getHeaders() != null) {
            this.getHeaders().putAll(headers);
        } else {
            setHeaders(headers);
        }
        return this;
    }
}
