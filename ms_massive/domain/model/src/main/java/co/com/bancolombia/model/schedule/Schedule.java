package co.com.bancolombia.model.schedule;

import co.com.bancolombia.model.commons.enums.ScheduleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Schedule {

    private Long id;
    private String idCampaign;
    private String idConsumer;
    private ScheduleType scheduleType;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate endDate;
    private LocalTime endTime;
    private String creationUser;
    private LocalDateTime createdDate;
    private String modifiedUser;
    private LocalDateTime modifiedDate;

}