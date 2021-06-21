package co.com.bancolombia.api.dto;

import co.com.bancolombia.model.alert.Alert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertDTO {

    @Size(max = 3, message = "{constraint.maximum_length}")
    @NotBlank(message = "{constraint.not_blank}")
    @NotNull(message = "{constraint.not_null}")
    private String id;
    @Max(value = 999, message = "{constraint.maximum_length}")
    @NotNull(message = "{constraint.not_null}")
    @PositiveOrZero(message = "{constraint.number_not_negative}")
    private Integer idTemplate;
    @Size(max = 3, message = "{constraint.maximum_length}")
    @NotBlank(message = "{constraint.not_blank}")
    @NotNull(message = "{constraint.not_null}")
    private String idProviderMail;
    @Size(max = 3, message = "{constraint.maximum_length}")
    @NotBlank(message = "{constraint.not_blank}")
    @NotNull(message = "{constraint.not_null}")
    private String idProviderSms;
    @Max(value = 999, message = "{constraint.maximum_length}")
    @NotNull(message = "{constraint.not_null}")
    @PositiveOrZero(message = "{constraint.number_not_negative}")
    private Integer idRemitter;
    @Max(value = 999, message = "{constraint.maximum_length}")
    @NotNull(message = "{constraint.not_null}")
    @PositiveOrZero(message = "{constraint.number_not_negative}")
    private Integer idService;
    @Max(value = 9, message = "{constraint.maximum_length}")
    @NotNull(message = "{constraint.not_null}")
    @PositiveOrZero(message = "{constraint.number_not_negative}")
    private Integer idState;
    @NotNull(message = "{constraint.not_null}")
    @PositiveOrZero(message = "{constraint.number_not_negative}")
    private Integer priority;
    @Size(max = 50, message = "{constraint.maximum_length}")
    @NotBlank(message = "{constraint.not_blank}")
    @NotNull(message = "{constraint.not_null}")
    private String description;
    @Size(max = 2, message = "{constraint.maximum_length}")
    @NotBlank(message = "{constraint.not_blank}")
    @NotNull(message = "{constraint.not_null}")
    private String nature;
    @Size(max = 500, message = "{constraint.maximum_length}")
    @NotBlank(message = "{constraint.not_blank}")
    @NotNull(message = "{constraint.not_null}")
    private String message;
    @Size(max = 50, message = "{constraint.maximum_length}")
    @NotBlank(message = "{constraint.not_blank}")
    @NotNull(message = "{constraint.not_null}")
    private String subjectMail;
    @Size(max = 15, message = "{constraint.maximum_length}")
    @NotBlank(message = "{constraint.not_blank}")
    @NotNull(message = "{constraint.not_null}")
    private String attentionLine;
    @Size(max = 100, message = "{constraint.maximum_length}")
    @NotBlank(message = "{constraint.not_blank}")
    @NotNull(message = "{constraint.not_null}")
    private String pathAttachedMail;
    @NotNull(message = "{constraint.not_null}")
    private Boolean obligatory;
    @NotNull(message = "{constraint.not_null}")
    private Boolean visibleChannel;
    @Size(max = 20, message = "{constraint.maximum_length}")
    @NotBlank(message = "{constraint.not_blank}")
    @NotNull(message = "{constraint.not_null}")
    private String creationUser;

    public Mono<Alert> toModel() {
        return Mono.just(Alert.builder()
                .id(this.id)
                .idProviderMail(this.idProviderMail)
                .idProviderSms(this.idProviderSms)
                .idRemitter(this.idRemitter)
                .idService(this.idService)
                .idTemplate(this.idTemplate)
                .idState(this.idState)
                .priority(this.priority)
                .description(this.description)
                .nature(this.nature)
                .message(this.message)
                .subjectMail(this.subjectMail)
                .attentionLine(this.attentionLine)
                .pathAttachedMail(this.pathAttachedMail)
                .obligatory(this.obligatory)
                .visibleChannel(this.visibleChannel)
                .creationUser(this.creationUser)
                .build());
    }
}
