package co.com.bancolombia.schedule.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table("schedule")
public class ScheduleData implements Persistable<Integer> {

    @Id
    private Integer id;
    private String idCampaign;
    private String idConsumer;
    private String scheduleType;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate endDate;
    private LocalTime endTime;
    private String creationUser;
    private LocalDateTime createdDate;

    @Transient
    private Boolean isNew;

    @Override
    @Transient
    public boolean isNew() {
        return this.isNew;
    }
}
