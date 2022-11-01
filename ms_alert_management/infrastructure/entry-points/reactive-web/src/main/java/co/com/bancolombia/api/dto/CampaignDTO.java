package co.com.bancolombia.api.dto;

import co.com.bancolombia.api.commons.validators.constraints.CheckBooleanValueForFieldRequired;
import co.com.bancolombia.api.commons.validators.constraints.JSONFieldNotNull;
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
        message = "attachmentPath must not be null",
        groups = {OnCreate.class, OnUpdate.class}
)
public class CampaignDTO extends DTO<Campaign> {

    @Size(min = 1, max = 50, message = "{constraint.size}", groups = {OnCreate.class, OnUpdate.class, OnDelete.class})
    @NotNull(message = "{constraint.not_null}", groups = {OnCreate.class, OnUpdate.class, OnDelete.class})
    private String idCampaign;

    @Size(min = 1, max = 10, message = "{constraint.size}", groups = {OnCreate.class, OnUpdate.class, OnDelete.class})
    @NotNull(message = "{constraint.not_null}", groups = {OnCreate.class, OnUpdate.class, OnDelete.class})
    private String idConsumer;

    @JSONFieldNotNull(groups = {OnCreate.class, OnUpdate.class})
    private JsonNode provider;

    @Min(value = 0, message = "{constraint.min}", groups = {OnCreate.class, OnUpdate.class})
    @NotNull(message = "{constraint.not_null}", groups = {OnCreate.class, OnUpdate.class})
    private Integer idRemitter;

    @Size(min = 1, max = 50, message = "{constraint.size}", groups = {OnCreate.class, OnUpdate.class})
    private String defaultTemplate;

    @Size(min = 1, max = 200, message = "{constraint.size}", groups = {OnCreate.class, OnUpdate.class})
    private String description;

    @Size(min = 1, max = 255, message = "{constraint.size}", groups = {OnCreate.class, OnUpdate.class})
    @NotNull(message = "{constraint.not_null}", groups = {OnCreate.class, OnUpdate.class})
    private String sourcePath;

    @NotNull(message = "{constraint.not_null}", groups = {OnCreate.class, OnUpdate.class})
    private Boolean attachment;

    @Size(min = 1, max = 255, message = "{constraint.size}", groups = {OnCreate.class, OnUpdate.class})
    private String attachmentPath;

    @NotNull(message = "{constraint.not_null}", groups = {OnCreate.class, OnUpdate.class})
    private Boolean dataEnrichment;

    @Pattern(regexp = "^0$|^1$", message = "allowed input: 0 or 1", groups = {OnCreate.class, OnUpdate.class})
    private String state;

    @Size(min = 1, max = 20, message = "{constraint.size}", groups = {OnCreate.class, OnUpdate.class})
    @NotNull(message = "{constraint.not_null}", groups = {OnCreate.class})
    private String creationUser;

    @Size(min = 1, max = 20, message = "{constraint.size}", groups = {OnCreate.class, OnUpdate.class, OnDelete.class})
    private String modifiedUser;

    @NotNull(message = "{constraint.not_null}", groups = {OnCreate.class})
    @Size(min = 1, max = 50, message = "must be between {min} and {max} items", groups = {OnCreate.class})
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
                .state(this.state)
                .creationUser(this.creationUser)
                .modifiedUser(this.modifiedUser)
                .schedules(this.schedulesToModel())
                .build());
    }
}
