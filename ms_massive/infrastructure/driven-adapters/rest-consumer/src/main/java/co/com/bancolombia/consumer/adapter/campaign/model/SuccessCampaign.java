package co.com.bancolombia.consumer.adapter.campaign.model;

import co.com.bancolombia.model.commons.enums.ScheduleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SuccessCampaign {
    private Data data;

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(toBuilder = true)
    public static class ScheduleResponse {
        private Long id;
        private String idCampaign;
        private String idConsumer;
        private ScheduleType scheduleType;
    }

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(toBuilder = true)
    public static class Data {
        private String idCampaign;
        private String idConsumer;
        private String state;
        private List<ScheduleResponse> schedules;
    }
}
