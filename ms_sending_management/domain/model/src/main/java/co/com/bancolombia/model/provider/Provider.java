package co.com.bancolombia.model.provider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Provider {
    private String id;
    private String name;
    private String typeService;
    private String creationUser;
    private LocalDateTime createdDate;
}
