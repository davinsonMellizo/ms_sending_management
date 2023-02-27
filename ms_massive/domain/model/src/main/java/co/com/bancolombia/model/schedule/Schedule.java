package co.com.bancolombia.model.schedule;

import co.com.bancolombia.model.commons.enums.ScheduleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Schedule {
    private Long id;
    private String idCampaign;
    private String idConsumer;
    private ScheduleType scheduleType;
}
