package co.com.bancolombia.model.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class Client {
    private Integer id;
    private Long documentNumber;
    private Integer idDocumentType;
    private String keyMdm;
    private String enrollmentOrigin;
    private Integer idState;
    private String creationUser;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
