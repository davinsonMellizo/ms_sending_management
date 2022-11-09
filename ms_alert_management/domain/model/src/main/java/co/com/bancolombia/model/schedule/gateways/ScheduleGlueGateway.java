package co.com.bancolombia.model.schedule.gateways;

import co.com.bancolombia.model.response.StatusResponse;
import co.com.bancolombia.model.schedule.Schedule;
import reactor.core.publisher.Mono;

public interface ScheduleGlueGateway {
    Mono<StatusResponse<Schedule>> updateSchedule(StatusResponse<Schedule> response);
}
