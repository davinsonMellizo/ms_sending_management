package co.com.bancolombia.model.alert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Alert implements Serializable {
    private String id;
    private String idProviderMail;
    private String idProviderSms;
    private String templateName;
    private Integer idRemitter;
    private Integer idState;
    private Integer priority;
    private Integer idCategory;
    private String description;
    private String nature;
    private String message;
    private String subjectMail;
    private String attentionLine;
    private String push;
    private Boolean obligatory;
    private Boolean basicKit;
    private Boolean visibleChannel;
    private String creationUser;
    private LocalDateTime createdDate;

    public Alert message(String message) {
        setMessage(message);
        return this;
    }

}
