package co.com.bancolombia.contact.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table("contact")
public class ContactData {

    @Id
    private Integer id;
    private String segment;
    @ReadOnlyProperty
    private String contactMedium;
    private Integer idContactMedium;
    private Long documentNumber;
    @Column("id_document_type")
    private Integer documentType;
    private String value;
    @ReadOnlyProperty
    private String state;
    private Integer idState;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}