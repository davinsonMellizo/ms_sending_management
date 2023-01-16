package co.com.bancolombia.api.dto;

import co.com.bancolombia.api.commons.validators.constraints.DateGreaterThan;
import co.com.bancolombia.api.commons.validators.constraints.FieldsValueMatch;
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
        message = "startTime y endTime no coinciden los valores"
)
@DateGreaterThan(
        startDate = "startDate",
        endDate = "endDate",
        message = "startDate debe ser posterior a endDate"
)
public class ScheduleDTO extends DTO<Schedule> {

    @Size(min = 1, max = 50, message = "debe tener entre {min} y {max} caracteres de longitud")
    @NotNull(message = "no debe ser nulo")
    private String idCampaign;

    @Size(min = 1, max = 10, message = "debe tener entre {min} y {max} caracteres de longitud")
    @NotNull(message = "no debe ser nulo")
    private String idConsumer;

    @NotNull(message = "no debe ser nulo")
    private ScheduleType scheduleType;

    @NotNull(message = "no debe ser nulo")
    private LocalDate startDate;

    @NotNull(message = "no debe ser nulo")
    private LocalTime startTime;

    private LocalDate endDate;

    private LocalTime endTime;

    @Size(min = 1, max = 20, message = "debe tener entre {min} y {max} caracteres de longitud")
    private String creationUser;

    @Size(min = 1, max = 20, message = "debe tener entre {min} y {max} caracteres de longitud")
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
