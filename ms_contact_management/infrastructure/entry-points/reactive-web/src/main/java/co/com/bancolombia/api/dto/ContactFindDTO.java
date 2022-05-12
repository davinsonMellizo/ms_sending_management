package co.com.bancolombia.api.dto;

import co.com.bancolombia.model.contact.Contact;
import co.com.bancolombia.model.contact.ResponseContacts;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class ContactFindDTO {

    private String segment;
    private String contactChannel;
    private String dataValue;
    private String status;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public ContactFindDTO(Contact contact) {
        this.segment = contact.getSegment();
        this.contactChannel = contact.getContactWay();
        this.dataValue = contact.getValue();
        this.status = contact.getStateContact();
        this.createdDate = contact.getCreatedDate();
        this.modifiedDate = contact.getModifiedDate();
    }
}