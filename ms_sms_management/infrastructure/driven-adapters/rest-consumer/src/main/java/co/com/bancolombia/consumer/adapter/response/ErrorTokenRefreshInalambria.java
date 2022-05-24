package co.com.bancolombia.consumer.adapter.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorTokenRefreshInalambria {
    private String type;
    private String title;
    private Integer status;
    private String traceId;

}
