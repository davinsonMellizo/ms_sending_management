package co.com.bancolombia.campaign.data;

import co.com.bancolombia.model.schedule.Schedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table("campaign")
public class CampaignData implements Persistable<Integer> {

    @Id
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

    @Transient
    private List<Schedule> schedules;

    @Transient
    private Boolean isNew;

    @Override
    @Transient
    public boolean isNew() {
        return this.isNew;
    }

}