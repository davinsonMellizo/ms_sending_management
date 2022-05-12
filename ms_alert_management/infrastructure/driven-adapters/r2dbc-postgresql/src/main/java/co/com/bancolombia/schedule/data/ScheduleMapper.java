package co.com.bancolombia.schedule.data;

import co.com.bancolombia.model.schedule.Schedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface ScheduleMapper {

    Schedule toEntity(ScheduleData scheduleData);

    @Mapping(target = "isNew", ignore = true)
    ScheduleData toData(Schedule schedule);
}