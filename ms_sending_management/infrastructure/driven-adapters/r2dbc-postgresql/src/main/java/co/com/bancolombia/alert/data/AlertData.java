package co.com.bancolombia.alert.data;

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
@Table("alert")
public class AlertData implements Persistable<String> {

    @Id
    private String id;
    private String idProviderMail;
    private String idProviderSms;
    private Integer idTemplate;
    private Integer idRemitter;
    private Integer idService;
    private Integer idState;
    private Integer priority;
    private String description;
    private String nature;
    private String message;
    private String subjectMail;
    private String attentionLine;
    private String pathAttachedMail;
    private Boolean obligatory;
    private Boolean visibleChannel;
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