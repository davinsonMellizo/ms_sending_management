package co.com.bancolombia.schedule.data;

import co.com.bancolombia.model.schedule.Schedule;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface ScheduleMapper {

    Schedule toEntity(ScheduleData scheduleData);

    ScheduleData toData(Schedule schedule);
}