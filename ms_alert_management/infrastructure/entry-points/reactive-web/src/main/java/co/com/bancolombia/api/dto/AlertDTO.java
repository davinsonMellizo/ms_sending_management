package co.com.bancolombia.api.dto;

import co.com.bancolombia.model.alert.Alert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertDTO extends DTO{

    @Size(min = 1, max = 3, message = "{constraint.size}")
    @NotNull(message = "{constraint.not_null}")
    private String id;
    @Size(min = 1, max = 100, message = "{constraint.size}")
    @NotNull(message = "{constraint.not_null}")
    private String templateName;
    @Size(min = 1, max = 4, message = "{constraint.size}")
    @NotNull(message = "{constraint.not_null}")
    private String idProviderMail;
    @Size(min = 1, max = 4, message = "{constraint.size}")
    @NotNull(message = "{constraint.not_null}")
    private String idProviderSms;
    @Max(value = 99, message = "{constraint.max}")
    @Min(value = 0, message = "{constraint.min}")
    @NotNull(message = "{constraint.not_null}")
    private Integer idRemitter;
    @Max(value = 99, message = "{constraint.max}")
    @Min(value = 0, message = "{constraint.min}")
    @NotNull(message = "{constraint.not_null}")
    private Integer idState;
    @Max(value = 99, message = "{constraint.max}")
    @Min(value = 0, message = "{constraint.min}")
    @NotNull(message = "{constraint.not_null}")
    private Integer priority;
    @Max(value = 99, message = "{constraint.max}")
    @Min(value = 0, message = "{constraint.min}")
    @NotNull(message = "{constraint.not_null}")
    private Integer idCategory;
    @Size(min = 1, max = 50, message = "{constraint.size}")
    @NotNull(message = "{constraint.not_null}")
    private String description;
    @Size(min = 1, max = 2, message = "{constraint.size}")
    @NotNull(message = "{constraint.not_null}")
    private String nature;
    @Size(min = 1, max = 500, message = "{constraint.size}")
    @NotNull(message = "{constraint.not_null}")
    private String message;
    @Size(min = 1, max = 50, message = "{constraint.size}")
    @NotNull(message = "{constraint.not_null}")
    private String subjectMail;
    @Size(min = 1, max = 15, message = "{constraint.size}")
    @NotNull(message = "{constraint.not_null}")
    private String attentionLine;
    @NotNull(message = "{constraint.not_null}")
    private Boolean obligatory;
    @NotNull(message = "{constraint.not_null}")
    private Boolean visibleChannel;
    @NotNull(message = "{constraint.not_null}")
    private Boolean basicKit;
    @Size(min = 2, max = 2, message = "{constraint.size}")
    @NotNull(message = "{constraint.not_null}")
    private String push;
    @Size(max = 20, message = "{constraint.size}")
    private String creationUser;

    @Override
    public Mono<Alert> toModel() {
        return Mono.just(Alert.builder()
                .id(this.id)
                .idProviderMail(this.idProviderMail)
                .idProviderSms(this.idProviderSms)
                .idRemitter(this.idRemitter)
                .templateName(this.templateName)
                .idState(this.idState)
                .priority(this.priority)
                .idCategory(this.idCategory)
                .description(this.description)
                .nature(this.nature)
                .message(this.message)
                .subjectMail(this.subjectMail)
                .attentionLine(this.attentionLine)
                .obligatory(this.obligatory)
                .basicKit(this.basicKit)
                .visibleChannel(this.visibleChannel)
                .push(this.push)
                .creationUser(this.creationUser)
                .build());
    }
}
