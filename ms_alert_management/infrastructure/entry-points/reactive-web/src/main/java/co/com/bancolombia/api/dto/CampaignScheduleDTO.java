package co.com.bancolombia.api.dto;

import co.com.bancolombia.api.commons.validators.constraints.DateGreaterThan;
import co.com.bancolombia.api.commons.validators.constraints.FieldsValueMatch;
import co.com.bancolombia.api.commons.validators.groups.OnCreate;
import co.com.bancolombia.commons.enums.ScheduleType;
import co.com.bancolombia.model.schedule.Schedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldsValueMatch(
        field = "startTime",
        fieldMatch = "endTime",
        message = "startTime y endTime no coinciden los valores",
        groups = {OnCreate.class}
)
@DateGreaterThan(
        startDate = "startDate",
        endDate = "endDate",
        message = "startDate debe ser posterior a endDate",
        groups = {OnCreate.class}
)
public class CampaignScheduleDTO {
    @NotNull(message = "no debe ser nulo", groups = {OnCreate.class})
    private ScheduleType scheduleType;

    @NotNull(message = "no debe ser nulo", groups = {OnCreate.class})
    private LocalDate startDate;

    @NotNull(message = "no debe ser nulo", groups = {OnCreate.class})
    private LocalTime startTime;

    private LocalDate endDate;

    private LocalTime endTime;

    public Schedule toModel() {
        return Schedule.builder()
                .scheduleType(this.scheduleType)
                .startDate(this.startDate)
                .startTime(this.startTime)
                .endDate(this.endDate)
                .endTime(this.endTime)
                .build();
    }
}
