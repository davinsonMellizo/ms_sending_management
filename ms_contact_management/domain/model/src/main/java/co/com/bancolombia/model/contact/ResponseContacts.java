package co.com.bancolombia.model.contact;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class ResponseContacts {
    private Long documentNumber;
    private String documentType;
    private List<Contact> contacts;

}
