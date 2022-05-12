package co.com.bancolombia.model.priority;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Priority {

    private Integer id;
    private Integer code;
    private String description;
    private String idProvider;
    private String creationUser;
    private LocalDateTime createdDate;
}
