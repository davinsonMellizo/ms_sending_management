package co.com.bancolombia.model.alert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Alert {
    private String id;
    private Integer idTemplate;
    private Integer idProviderMail;
    private Integer idProviderSms;
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
}