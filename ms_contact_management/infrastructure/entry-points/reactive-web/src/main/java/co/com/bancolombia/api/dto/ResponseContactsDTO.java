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
        this.customer.setIdentification(IdentificationDTO.builder()
                .documentNumber(responseContacts.getDocumentNumber())
                .documentType(responseContacts.getDocumentType()).build());
        this.customer.setTraceability(ResponseTraceabilityDTO.builder()
                .mdmKey(responseContacts.getKeyMdm())
                .enrollmentOrigin(responseContacts.getEnrollmentOrigin())
                .status(responseContacts.getStatus())
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

    private IdentificationDTO identification;
    private ResponseTraceabilityDTO traceability;
}

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
class ResponseTraceabilityDTO {

    private String mdmKey;
    private String enrollmentOrigin;
    private String status;
    private String creationUser;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}

