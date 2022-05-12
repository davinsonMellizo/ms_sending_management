package co.com.bancolombia.model.campaign;

import co.com.bancolombia.model.schedule.Schedule;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Campaign {

    private Integer id;
    private String idCampaign;
    private String idConsumer;
    private String idProvider;
    private Integer idRemitter;
    private String defaultTemplate;
    private String description;
    private String sourcePath;
    private boolean attachment;
    private String attachmentPath;
    private String state;
    private String creationUser;
    private LocalDateTime createdDate;

    @With
    private List<Schedule> schedules;

}
