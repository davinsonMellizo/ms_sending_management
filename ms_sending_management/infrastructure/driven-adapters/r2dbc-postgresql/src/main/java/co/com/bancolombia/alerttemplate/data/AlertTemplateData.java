package co.com.bancolombia.alerttemplate.data;

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
@Table("alert_template")
public class AlertTemplateData implements Persistable<Integer> {

    @Id
    private Integer id;
    private String field;
    private Integer initialPosition;
    private Integer finalPosition;
    private String creationUser;
    private LocalDateTime createdDate;

    @Transient
    private Boolean isNew;

    @Override
    @Transient
    public boolean isNew() {
        return this.isNew;
    }
}
