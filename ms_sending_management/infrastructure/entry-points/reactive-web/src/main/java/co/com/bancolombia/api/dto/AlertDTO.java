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
public class AlertDTO {

    @Size(min = 1, max = 3, message = "{constraint.size}")
    @NotNull(message = "{constraint.not_null}")
    private String id;
    @Max(value = 999, message = "{constraint.max}")
    @Min(value = 0, message = "{constraint.min}")
    @NotNull(message = "{constraint.not_null}")
    private Integer idTemplate;
    @Max(value = 999, message = "{constraint.max}")
    @Min(value = 0, message = "{constraint.min}")
    @NotNull(message = "{constraint.not_null}")
    private Integer idProviderMail;
    @Max(value = 999, message = "{constraint.max}")
    @Min(value = 0, message = "{constraint.min}")
    @NotNull(message = "{constraint.not_null}")
    private Integer idProviderSms;
    @Max(value = 999, message = "{constraint.max}")
    @Min(value = 0, message = "{constraint.min}")
    @NotNull(message = "{constraint.not_null}")
    private Integer idRemitter;
    @Max(value = 999, message = "{constraint.max}")
    @Min(value = 0, message = "{constraint.min}")
    @NotNull(message = "{constraint.not_null}")
    private Integer idService;
    @Max(value = 9, message = "{constraint.max}")
    @Min(value = 0, message = "{constraint.min}")
    @NotNull(message = "{constraint.not_null}")
    private Integer idState;
    @Max(value = 999, message = "{constraint.max}")
    @Min(value = 0, message = "{constraint.min}")
    @NotNull(message = "{constraint.not_null}")
    private Integer priority;
    @Max(value = 999, message = "{constraint.max}")
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
    @Size(min = 1, max = 100, message = "{constraint.size}")
    @NotNull(message = "{constraint.not_null}")
    private String pathAttachedMail;
    @NotNull(message = "{constraint.not_null}")
    private Boolean obligatory;
    @NotNull(message = "{constraint.not_null}")
    private Boolean visibleChannel;
    @Size(min = 2, max = 2, message = "{constraint.size}")
    @NotNull(message = "{constraint.not_null}")
    private String push;
    @Size(max = 20, message = "{constraint.size}")
    private String creationUser;

    public Mono<Alert> toModel() {
        return Mono.just(Alert.builder()
                .id(this.id)
                .idProviderMail(this.idProviderMail)
                .idProviderSms(this.idProviderSms)
                .idRemitter(this.idRemitter)
                .idTemplate(this.idTemplate)
                .idState(this.idState)
                .priority(this.priority)
                .idCategory(this.idCategory)
                .description(this.description)
                .nature(this.nature)
                .message(this.message)
                .subjectMail(this.subjectMail)
                .attentionLine(this.attentionLine)
                .pathAttachedMail(this.pathAttachedMail)
                .obligatory(this.obligatory)
                .visibleChannel(this.visibleChannel)
                .push(this.push)
                .creationUser(this.creationUser)
                .build());
    }
}
