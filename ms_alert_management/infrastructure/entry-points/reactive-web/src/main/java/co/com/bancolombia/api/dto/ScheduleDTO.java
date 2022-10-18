package co.com.bancolombia.api.dto;

import co.com.bancolombia.api.commons.validators.constraints.FieldsValueMatch;
import co.com.bancolombia.api.commons.validators.groups.OnCreate;
import co.com.bancolombia.api.commons.validators.groups.OnUpdate;
import co.com.bancolombia.commons.enums.ScheduleType;
import co.com.bancolombia.model.schedule.Schedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldsValueMatch(
        field = "startTime",
        fieldMatch = "endTime",
        message = "startTime and endTime fields values don't match"
)
public class ScheduleDTO extends DTO<Schedule> {

    @Size(min = 1, max = 50, message = "{constraint.size}")
    @NotNull(message = "{constraint.not_null}")
    private String idCampaign;

    @Size(min = 1, max = 10, message = "{constraint.size}")
    @NotNull(message = "{constraint.not_null}")
    private String idConsumer;

    @NotNull(message = "{constraint.not_null}")
    private ScheduleType scheduleType;

    @NotNull(message = "{constraint.not_null}")
    private LocalDate startDate;

    @NotNull(message = "{constraint.not_null}")
    private LocalTime startTime;

    private LocalDate endDate;

    private LocalTime endTime;

    @Size(min = 1, max = 20, message = "{constraint.size}")
    private String creationUser;

    @Size(min = 1, max = 20, message = "{constraint.size}")
    private String modifiedUser;

    public Mono<Schedule> toModel() {
        return Mono.just(Schedule.builder()
                .idCampaign(this.idCampaign)
                .idConsumer(this.idConsumer)
                .scheduleType(this.scheduleType)
                .startDate(this.startDate)
                .startTime(this.startTime)
                .endDate(this.endDate)
                .endTime(this.endTime)
                .creationUser(this.creationUser)
                .modifiedUser(this.modifiedUser)
                .build());
    }
}
