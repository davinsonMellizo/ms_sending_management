package co.com.bancolombia.schedule;

import co.com.bancolombia.AdapterOperations;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.drivenadapters.TimeFactory;
import co.com.bancolombia.model.campaign.Campaign;
import co.com.bancolombia.model.response.StatusResponse;
import co.com.bancolombia.model.schedule.Schedule;
import co.com.bancolombia.model.schedule.gateways.ScheduleGateway;
import co.com.bancolombia.schedule.data.ScheduleData;
import co.com.bancolombia.schedule.data.ScheduleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.SAVE_CAMPAIGN_ERROR;
import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.SAVE_SCHEDULE_ERROR;
import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.FIND_CAMPAIGN_BY_ID_ERROR;
import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.FIND_SCHEDULE_BY_ID_ERROR;
import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.UPDATE_SCHEDULE_ERROR;

@Repository
public class ScheduleRepositoryImplement extends AdapterOperations<Schedule, ScheduleData, Long, ScheduleRepository>
        implements ScheduleGateway {

    @Autowired
    public ScheduleRepositoryImplement(ScheduleRepository repository, ScheduleMapper mapper) {
        super(repository, mapper::toData, mapper::toEntity);
    }

    @Autowired
    private TimeFactory timeFactory;

    @Override
    public Mono<Schedule> saveSchedule(Schedule schedule) {
        return Mono.just(schedule)
                .map(this::convertToData)
                .map(scheduleData -> scheduleData.toBuilder()
                        .createdDate(timeFactory.now())
                        .modifiedUser(null)
                        .build())
                .flatMap(repository::save)
                .map(this::convertToEntity)
                .onErrorMap(e -> new TechnicalException(e, SAVE_SCHEDULE_ERROR));
    }

    @Override
    public Mono<Schedule> findScheduleById(Long id) {
        return repository.findById(id).map(this::convertToEntity)
                .onErrorMap(e -> new TechnicalException(e, FIND_SCHEDULE_BY_ID_ERROR));
    }

    @Override
    public Mono<StatusResponse<Schedule>> updateSchedule(Schedule schedule, Long id) {
        return repository.findById(id)
                .map(this::convertToEntity)
                .map(scheduleFound -> StatusResponse.<Schedule>builder()
                        .before(scheduleFound)
                        .actual(schedule)
                        .build())
                .flatMap(this::update);
    }

    private Mono<StatusResponse<Schedule>> update(StatusResponse<Schedule> response) {
        return Mono.just(response.getActual())
                .map(this::convertToData)
                .map(data -> data.toBuilder()
                        .id(response.getBefore().getId())
                        .createdDate(response.getBefore().getCreatedDate())
                        .creationUser(response.getBefore().getCreationUser())
                        .modifiedDate(timeFactory.now())
                        .build())
                .flatMap(repository::save)
                .map(this::convertToEntity)
                .map(actual -> response.toBuilder().actual(actual).description("Actualizacion exitosa").build())
                .onErrorMap(e -> new TechnicalException(e, UPDATE_SCHEDULE_ERROR));
    }

    @Override
    public Mono<Campaign> findSchedulesByCampaign(Campaign campaign) {
        return Mono.zip(
                        Mono.just(campaign),
                        repository.findSchedulesByCampaign(campaign.getIdCampaign(), campaign.getIdConsumer()).collectList(),
                        (t1, t2) -> t1.withSchedules(t2.stream().map(this::convertToEntity).collect(Collectors.toList())))
                .onErrorMap(e -> new TechnicalException(e, FIND_CAMPAIGN_BY_ID_ERROR));
    }

    @Override
    public Mono<Campaign> saveSchedulesByCampaign(Campaign campaign) {
        return Mono.just(campaign.getSchedules())
                .map(schedules -> schedules.stream().map(this::convertToData)
                        .map(scheduleData -> scheduleData.toBuilder()
                                .idCampaign(campaign.getIdCampaign())
                                .idConsumer(campaign.getIdConsumer())
                                .creationUser(campaign.getCreationUser())
                                .createdDate(timeFactory.now())
                                .build())
                        .collect(Collectors.toList()))
                .flatMap(scheduleList -> repository.saveAll(scheduleList)
                        .map(this::convertToEntity)
                        .collectList()
                        .map(campaign::withSchedules))
                .onErrorMap(e -> new TechnicalException(e, SAVE_CAMPAIGN_ERROR));
    }

    @Override
    public Mono<Campaign> updateSchedulesByCampaign(Campaign campaign) {
        return Mono.just(campaign.getSchedules())
                .map(schedules -> schedules.stream().map(this::convertToData)
                        .map(scheduleData -> scheduleData.toBuilder()
                                .idCampaign(campaign.getIdCampaign())
                                .idConsumer(campaign.getIdConsumer())
                                .creationUser(campaign.getCreationUser())
                                .createdDate(campaign.getCreatedDate())
                                .modifiedDate(timeFactory.now())
                                .build())
                        .collect(Collectors.toList()))
                .flatMap(scheduleList -> repository.saveAll(scheduleList)
                        .map(this::convertToEntity)
                        .collectList()
                        .map(campaign::withSchedules))
                .onErrorMap(e -> new TechnicalException(e, SAVE_CAMPAIGN_ERROR));
    }
}
