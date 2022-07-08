package co.com.bancolombia.model.contact;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class ResponseContacts {

    private Long documentNumber;
    private String documentType;
    private String keyMdm;
    private Integer preference;
    private Boolean delegate;
    private String enrollmentOrigin;
    private String status;
    private String creationUser;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private List<Contact> contacts;
}
