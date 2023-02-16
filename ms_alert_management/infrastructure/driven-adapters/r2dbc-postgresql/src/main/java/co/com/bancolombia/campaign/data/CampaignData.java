package co.com.bancolombia.campaign.data;

import co.com.bancolombia.model.schedule.Schedule;
import io.r2dbc.postgresql.codec.Json;
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
public class CampaignData implements Persistable<String> {

    @Id
    private String idCampaign;
    private String idConsumer;
    private Json provider;
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
    private Integer priority;
    @Transient
    private List<Schedule> schedules;

    @Override
    public String getId() {
        return this.getIdCampaign();
    }

    @Override
    public boolean isNew() {
        return true;
    }

}
