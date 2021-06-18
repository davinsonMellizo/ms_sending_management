package co.com.bancolombia.model.remitter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Remitter {
    private Integer id;
    private String mail;
    private String state;
    private String creationUser;
    private LocalDateTime createdDate;
}
