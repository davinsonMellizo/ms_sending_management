package co.com.bancolombia.client.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table("client")
public class ClientData{
    @Id
    private Integer id;
    private Long documentNumber;
    @Column("id_document_type")
    private Integer documentType;
    private String keyMdm;
    private String enrollmentOrigin;
    private Integer idState;
    private String creationUser;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

}
