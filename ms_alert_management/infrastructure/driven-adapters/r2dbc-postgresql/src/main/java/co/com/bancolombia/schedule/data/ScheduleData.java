package co.com.bancolombia.schedule.data;

import co.com.bancolombia.commons.enums.ScheduleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table("schedule")
public class ScheduleData {

    @Id
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
