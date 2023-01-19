package co.com.bancolombia.provider.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table("provider")
public class ProviderData {
    @Id
    private String id;
    private String name;
    private String typeService;
    private String creationUser;
    private LocalDateTime createdDate;

    @Transient
    private Boolean isNew;

}
