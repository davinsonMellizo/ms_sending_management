package co.com.bancolombia.model.campaign;

import co.com.bancolombia.model.schedule.Schedule;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Campaign {
    private String idCampaign;
    private String idConsumer;
    private String state;
    @With
    private List<Schedule> schedules;
}
