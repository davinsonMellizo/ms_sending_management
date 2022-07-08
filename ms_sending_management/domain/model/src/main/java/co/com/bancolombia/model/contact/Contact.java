package co.com.bancolombia.model.contact;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Contact {
    private Integer id;
    private String enrollmentContact;
    private String contactMedium;
    private Integer idEnrollmentContact;
    private Integer idContactMedium;
    private Long documentNumber;
    private Integer documentType;
    private String value;
    private String state;
    private Integer idState;
    private Boolean previous;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
