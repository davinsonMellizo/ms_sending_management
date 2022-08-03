package co.com.bancolombia.api.dto;

import co.com.bancolombia.model.contact.ResponseContacts;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class ResponseContactsDTO {

    private ResponseCustomerDTO customer;
    private List<ContactFindDTO> contactData;

    public ResponseContactsDTO(ResponseContacts responseContacts) {
        this.customer = new ResponseCustomerDTO();
        this.contactData = new ArrayList<>();
        this.customer.setMdmKey(responseContacts.getKeyMdm());
        this.customer.setStatus(responseContacts.getStatus());
        this.customer.setPreference(responseContacts.getPreference());
        this.customer.setDelegate(responseContacts.getDelegate());
        this.customer.setIdentification(ClientIdentificationDTO.builder()
                .documentNumber(responseContacts.getDocumentNumber())
                .documentType(responseContacts.getDocumentType()).build());
        this.customer.setTraceability(ResponseTraceabilityDTO.builder()
                .enrollmentOrigin(responseContacts.getEnrollmentOrigin())
                .creationUser(responseContacts.getCreationUser())
                .createdDate(responseContacts.getCreatedDate())
                .modifiedDate(responseContacts.getModifiedDate()).build());
        responseContacts.getContacts().forEach(contact -> this.contactData.add(new
                ContactFindDTO(contact)));
    }
}

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
class ResponseCustomerDTO {

    private ClientIdentificationDTO identification;
    private ResponseTraceabilityDTO traceability;
    private String status;
    private String mdmKey;
    private Integer preference;
    private Boolean delegate;
}

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
class ResponseTraceabilityDTO {

    private String enrollmentOrigin;
    private String creationUser;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}

