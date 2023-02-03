package co.com.bancolombia.model.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Response {
    private Integer code;
    private String description;
    private String token;
    private List<SMSInfobip.Response> messages;
    private List<SMSInfobip.RequestError> requestError;
}
