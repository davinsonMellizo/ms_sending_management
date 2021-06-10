package co.com.bancolombia.model.response;

import co.com.bancolombia.model.alert.Alert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class StatusResponse {
    private String description;
    private Alert before;
    private Alert actual;
}
