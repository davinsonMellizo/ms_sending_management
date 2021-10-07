package co.com.bancolombia.model.contact;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Contact {
    private Integer id;
    private String segment;
    private String contactMedium;
    private Long documentNumber;
    private String documentType;
    private String value;
    private String state;
    private Boolean previous;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

}
