package co.com.bancolombia.model.campaign;

import co.com.bancolombia.model.schedule.Schedule;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;

import java.time.LocalDateTime;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Campaign {

    private String idCampaign;
    private String idConsumer;
    private String provider;
    private Integer idRemitter;
    private String defaultTemplate;
    private String description;
    private String sourcePath;
    private boolean attachment;
    private String attachmentPath;
    private Boolean dataEnrichment;
    private String state;
    private String creationUser;
    private LocalDateTime createdDate;
    private String modifiedUser;
    private LocalDateTime modifiedDate;

    @With
    private List<Schedule> schedules;

}
