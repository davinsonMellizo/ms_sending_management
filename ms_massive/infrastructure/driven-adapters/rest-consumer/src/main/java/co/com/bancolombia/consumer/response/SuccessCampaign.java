package co.com.bancolombia.consumer.response;

import co.com.bancolombia.commons.enums.ScheduleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
        private LocalDate startDate;
        private LocalTime startTime;
        private LocalDate endDate;
        private LocalTime endTime;
        private String creationUser;
        private LocalDateTime createdDate;
        private String modifiedUser;
        private LocalDateTime modifiedDate;
    }

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(toBuilder = true)
    public static class CampaignResponse {
        private String idCampaign;
        private String idConsumer;
        private String provider;
        private Integer idRemitter;
        private String defaultTemplate;
        private String description;
        private String sourcePath;
        private boolean attachment;
        private String attachmentPath;
        private boolean dataEnrichment;
        private String state;
        private String creationUser;
        private LocalDateTime createdDate;
        private String modifiedUser;
        private LocalDateTime modifiedDate;

        private List<ScheduleResponse> schedules;
    }

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(toBuilder = true)
    public static class Data {
        private CampaignResponse campaignResponse;
    }
}
