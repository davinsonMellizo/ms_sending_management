package co.com.bancolombia.priority.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table("priority")
public class PriorityData {
    @Id
    private Integer id;
    private Integer code;
    private String description;
    private String idProvider;
    private String creationUser;
    private LocalDateTime createdDate;

    @Transient
    private Boolean isNew;

}
