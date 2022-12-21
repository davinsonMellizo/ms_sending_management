package co.com.bancolombia;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
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
