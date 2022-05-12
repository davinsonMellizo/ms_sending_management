package co.com.bancolombia.api.dto;

import co.com.bancolombia.model.schedule.Schedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleDTO {

    @Min(value = 1, message = "{constraint.min}")
    private Integer id;

    @Size(min = 1, max = 5, message = "{constraint.size}")
    private String idCampaign;

    @Size(min = 1, max = 3, message = "{constraint.size}")
    private String idConsumer;

    @Size(min = 1, max = 10, message = "{constraint.size}")
    @NotNull(message = "{constraint.not_null}")
    private String scheduleType;

    @NotNull(message = "{constraint.not_null}")
    private LocalDate startDate;

    @NotNull(message = "{constraint.not_null}")
    private LocalTime startTime;

    @NotNull(message = "{constraint.not_null}")
    private LocalDate endDate;

    @NotNull(message = "{constraint.not_null}")
    private LocalTime endTime;

    private String creationUser;

    public Schedule toModel() {
        return Schedule.builder()
                .id(this.id)
                .idCampaign(this.idCampaign)
                .idConsumer(this.idConsumer)
                .scheduleType(this.scheduleType)
                .startDate(this.startDate)
                .startTime(this.startTime)
                .endDate(this.endDate)
                .endTime(this.endTime)
                .creationUser(this.creationUser)
                .build();
    }
}
