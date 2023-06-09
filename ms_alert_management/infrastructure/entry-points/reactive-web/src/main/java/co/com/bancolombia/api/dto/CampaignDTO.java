package co.com.bancolombia.api.dto;

import co.com.bancolombia.api.commons.validators.constraints.CheckBooleanValueForFieldRequired;
import co.com.bancolombia.api.commons.validators.constraints.JSONFieldNotNull;
import co.com.bancolombia.api.commons.validators.constraints.ProviderFormat;
import co.com.bancolombia.api.commons.validators.groups.OnCreate;
import co.com.bancolombia.api.commons.validators.groups.OnDelete;
import co.com.bancolombia.api.commons.validators.groups.OnUpdate;
import co.com.bancolombia.model.campaign.Campaign;
import co.com.bancolombia.model.schedule.Schedule;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@CheckBooleanValueForFieldRequired(
        checkedField = "attachment",
        checkedValue = true,
        requiredField = "attachmentPath",
        message = "attachmentPath no debe ser nulo",
        groups = {OnCreate.class, OnUpdate.class}
)

public class CampaignDTO extends DTO<Campaign> {

    @Size(min = 1, max = 50, groups = {OnCreate.class, OnUpdate.class, OnDelete.class},
            message = "debe tener entre {min} y {max} caracteres de longitud")
    @NotNull(groups = {OnCreate.class, OnUpdate.class, OnDelete.class}, message = "no debe ser nulo")
    private String idCampaign;

    @Size(min = 1, max = 10, groups = {OnCreate.class, OnUpdate.class, OnDelete.class},
            message = "debe tener entre {min} y {max} caracteres de longitud")
    @NotNull(groups = {OnCreate.class, OnUpdate.class, OnDelete.class}, message = "no debe ser nulo")
    private String idConsumer;

    @JSONFieldNotNull(groups = {OnCreate.class, OnUpdate.class}, message = "no debe ser nulo o vacio")
    @ProviderFormat(groups = {OnCreate.class, OnUpdate.class})
    private JsonNode provider;

    @Min(value = 0, groups = {OnCreate.class, OnUpdate.class}, message = "debe ser mayor que {value}")
    @NotNull(groups = {OnCreate.class, OnUpdate.class}, message = "no debe ser nulo")
    private Integer idRemitter;

    @Size(min = 1, max = 50, groups = {OnCreate.class, OnUpdate.class},
            message = "debe tener entre {min} y {max} caracteres de longitud")
    private String defaultTemplate;

    @Size(min = 1, max = 200, groups = {OnCreate.class, OnUpdate.class},
            message = "debe tener entre {min} y {max} caracteres de longitud")
    private String description;

    @Size(min = 1, max = 255, groups = {OnCreate.class, OnUpdate.class},
            message = "debe tener entre {min} y {max} caracteres de longitud")
    @NotNull(groups = {OnCreate.class, OnUpdate.class}, message = "no debe ser nulo")
    private String sourcePath;

    @NotNull(groups = {OnCreate.class, OnUpdate.class}, message = "no debe ser nulo")
    private Boolean attachment;

    @Size(min = 1, max = 255, groups = {OnCreate.class, OnUpdate.class},
            message = "debe tener entre {min} y {max} caracteres de longitud")
    private String attachmentPath;

    @NotNull(groups = {OnCreate.class, OnUpdate.class}, message = "no debe ser nulo")
    private Boolean dataEnrichment;

    @Range(min = 0, max = 5, groups = {OnCreate.class, OnUpdate.class},
            message = "el valor debe estar entre {min} y {max}")
    @NotNull(groups = {OnCreate.class, OnUpdate.class}, message = "no debe ser nulo")
    private Integer priority;

    @Pattern(regexp = "^0$|^1$", message = "debe ser 0 o 1", groups = {OnCreate.class, OnUpdate.class})
    private String state;

    @Size(min = 1, max = 20, groups = {OnCreate.class, OnUpdate.class},
            message = "debe tener entre {min} y {max} caracteres de longitud")
    @NotNull(groups = {OnCreate.class}, message = "no debe ser nulo")
    private String creationUser;

    @Size(min = 1, max = 20, groups = {OnCreate.class, OnUpdate.class, OnDelete.class},
            message = "debe tener entre {min} y {max} caracteres de longitud")
    private String modifiedUser;

    @NotNull(groups = {OnCreate.class}, message = "no debe ser nulo")
    @Size(min = 1, max = 50, groups = {OnCreate.class}, message = "debe tener entre {min} y {max} horarios")
    private @Valid List<CampaignScheduleDTO> schedules;

    private List<Schedule> schedulesToModel() {
        return this.schedules != null && !this.schedules.isEmpty() ?
                this.schedules.stream().map(CampaignScheduleDTO::toModel).collect(Collectors.toList()) : null;
    }

    @Override
    public Mono<Campaign> toModel() {
        return Mono.just(Campaign.builder()
                .idCampaign(this.idCampaign)
                .idConsumer(this.idConsumer)
                .provider(this.provider != null ? this.provider.toString() : null)
                .idRemitter(this.idRemitter)
                .defaultTemplate(this.defaultTemplate)
                .description(this.description)
                .sourcePath(this.sourcePath)
                .attachment(this.attachment != null && this.attachment)
                .attachmentPath(this.attachmentPath)
                .dataEnrichment(this.dataEnrichment != null && this.dataEnrichment)
                .priority(this.priority)
                .state(this.state)
                .creationUser(this.creationUser)
                .modifiedUser(this.modifiedUser)
                .schedules(this.schedulesToModel())
                .build());
    }
}
