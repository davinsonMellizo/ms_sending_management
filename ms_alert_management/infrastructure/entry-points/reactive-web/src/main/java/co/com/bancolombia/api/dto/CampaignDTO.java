package co.com.bancolombia.api.dto;

import co.com.bancolombia.model.campaign.Campaign;
import co.com.bancolombia.model.schedule.Schedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignDTO {

    @Size(min = 1, max = 5, message = "{constraint.size}")
    @NotNull(message = "{constraint.not_null}")
    private String idCampaign;

    @Size(min = 1, max = 3, message = "{constraint.size}")
    @NotNull(message = "{constraint.not_null}")
    private String idConsumer;

    @Size(min = 1, max = 3, message = "{constraint.size}")
    @NotNull(message = "{constraint.not_null}")
    private String idProvider;

    @NotNull(message = "{constraint.not_null}")
    private Integer idRemitter;

    @Size(min = 1, max = 10, message = "{constraint.size}")
    @NotNull(message = "{constraint.not_null}")
    private String defaultTemplate;

    @Size(min = 1, max = 200, message = "{constraint.size}")
    private String description;

    @Size(min = 1, max = 200, message = "{constraint.size}")
    @NotNull(message = "{constraint.not_null}")
    private String sourcePath;

    @NotNull(message = "{constraint.not_null}")
    private boolean attachment;

    @Size(min = 1, max = 200, message = "{constraint.size}")
    @NotNull(message = "{constraint.not_null}")
    private String attachmentPath;

    @Size(min = 1, max = 10, message = "{constraint.size}")
    private String state;

    @Size(min = 1, max = 20, message = "{constraint.size}")
    @NotNull(message = "{constraint.not_null}")
    private String creationUser;

    @NotNull(message = "{constraint.not_null}")
    @Size(min = 1, max = 15, message = "{constraint.size}")
    private @Valid List<ScheduleDTO> schedules;

    private List<Schedule> schedulesToModel() {
        return this.schedules != null && !this.schedules.isEmpty() ?
                this.schedules.stream().map(ScheduleDTO::toModel).collect(Collectors.toList()) : null;
    }

    public Mono<Campaign> toModel() {
        return Mono.just(Campaign.builder()
                .idCampaign(this.idCampaign)
                .idConsumer(this.idConsumer)
                .idProvider(this.idProvider)
                .idRemitter(this.idRemitter)
                .defaultTemplate(this.defaultTemplate)
                .description(this.description)
                .sourcePath(this.sourcePath)
                .attachment(this.attachment)
                .attachmentPath(this.attachmentPath)
                .state(this.state)
                .creationUser(this.creationUser)
                .schedules(this.schedulesToModel())
                .build());
    }
}
