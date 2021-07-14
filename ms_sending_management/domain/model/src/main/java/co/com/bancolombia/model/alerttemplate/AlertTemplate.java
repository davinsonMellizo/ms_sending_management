package co.com.bancolombia.model.alerttemplate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AlertTemplate {

    private Integer id;
    private String field;
    private Integer initialPosition;
    private Integer finalPosition;
    private String creationUser;
    private LocalDateTime createdDate;

}
