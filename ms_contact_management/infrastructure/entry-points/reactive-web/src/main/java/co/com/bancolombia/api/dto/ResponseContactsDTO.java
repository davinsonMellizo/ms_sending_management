package co.com.bancolombia.api.dto;

import co.com.bancolombia.model.contact.ResponseContacts;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @NotNull(message = "{constraint.not_null}")
    @Size(min = 1, max = 10, message = "{constraint.size}")
    @Schema(allowableValues = {"Activo", "Inactivo"})
    private String status;
    @NotNull(message = "{constraint.not_null}")
    @Size(min = 0, max = 20, message = "{constraint.size}")
    @Schema(allowableValues = {"Activo", "Inactivo"})
    private String mdmKey;
    private Integer preference;
    @Schema(allowableValues = {"true", "false"})
    private Boolean delegate;
}

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
class ResponseTraceabilityDTO {

    private String enrollmentOrigin;
    @Size(max = 20, message = "{constraint.size}")
    private String creationUser;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}

