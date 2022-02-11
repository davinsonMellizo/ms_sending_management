package co.com.bancolombia.model.message;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageIbmMqResponse {

    private String correlationId;
    private String message;

}
