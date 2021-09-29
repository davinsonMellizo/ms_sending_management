package co.com.bancolombia.providerservice.data;

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
@Table("provider_service")
public class ProviderServiceData implements Persistable<Integer> {
    @Id
    private Integer id;
    private String idProvider;
    private Integer idService;

    private String code;
    private String description;
    private String service;

    @Transient
    private Boolean isNew;

    @Override
    @Transient
    public boolean isNew() {
        return this.id == null ? true : false;
    }
}
