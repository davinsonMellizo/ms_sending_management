package co.com.bancolombia.schedule;

import co.com.bancolombia.AdapterOperations;
import co.com.bancolombia.campaign.data.CampaignMapper;
import co.com.bancolombia.commons.enums.ScheduleType;
import co.com.bancolombia.commons.exceptions.BusinessException;
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
import reactor.util.function.Tuple2;

import java.util.List;
import java.util.stream.Collectors;

import static co.com.bancolombia.commons.enums.BusinessErrorMessage.*;
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

    @Autowired
    private CampaignMapper campaignMapper;

    public Mono<Campaign> saveSchedule(Schedule schedule) {
        return repository.findCampaignById(schedule.getIdCampaign(), schedule.getIdConsumer())
                .switchIfEmpty(Mono.error(new BusinessException(CAMPAIGN_NOT_FOUND)))
                .map(campaignMapper::toEntity)
                .flatMap(c -> this.validateSchedulesOnDemandByCampaign(c, schedule))
                .flatMap(c -> this.save(c, schedule));
    }

    private Mono<Campaign> validateSchedulesOnDemandByCampaign(Campaign campaign, Schedule schedule) {
        return Mono.just(schedule)
                .filter(sch -> ScheduleType.ON_DEMAND.equals(sch.getScheduleType()))
                .flatMap(sch -> repository.findSchedulesByTypeByCampaign(
                                        campaign.getIdCampaign(), campaign.getIdConsumer(), ScheduleType.ON_DEMAND
                                )
                                .collectList()
                                .filter(List::isEmpty)
                                .map(scheduleData -> campaign)
                                .switchIfEmpty(Mono.error(new BusinessException(CAMPAIGN_WITH_SCHEDULE_ON_DEMAND)))
                )
                .defaultIfEmpty(campaign);
    }

    private Mono<Campaign> save(Campaign campaign, Schedule schedule) {
        return Mono.just(schedule)
                .map(this::convertToData)
                .map(scheduleData -> scheduleData.toBuilder()
                        .createdDate(timeFactory.now())
                        .modifiedUser(null)
                        .build())
                .flatMap(repository::save)
                .map(this::convertToEntity)
                .map(scheduleSave -> campaign.toBuilder().schedules(List.of(scheduleSave)).build())
                .onErrorMap(e -> new TechnicalException(e, SAVE_SCHEDULE_ERROR));
    }

    @Override
    public Mono<Schedule> findScheduleById(Long id) {
        return repository.findById(id).map(this::convertToEntity)
                .onErrorMap(e -> new TechnicalException(e, FIND_SCHEDULE_BY_ID_ERROR));
    }

    @Override
    public Mono<StatusResponse<Campaign>> updateSchedule(Schedule schedule, Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new BusinessException(SCHEDULE_NOT_FOUND)))
                .map(this::convertToEntity)
                .zipWith(repository.findCampaignById(schedule.getIdCampaign(), schedule.getIdConsumer())
                        .map(campaignMapper::toEntity))
                .switchIfEmpty(Mono.error(new BusinessException(CAMPAIGN_NOT_FOUND)))
                .flatMap(t -> this.update(t, schedule));
    }

    private Mono<StatusResponse<Campaign>> update(Tuple2<Schedule, Campaign> t, Schedule schedule) {
        return Mono.just(schedule)
                .map(this::convertToData)
                .map(data -> data.toBuilder()
                        .id(t.getT1().getId())
                        .idCampaign(schedule.getIdCampaign())
                        .idConsumer(schedule.getIdConsumer())
                        .createdDate(t.getT1().getCreatedDate())
                        .creationUser(t.getT1().getCreationUser())
                        .modifiedDate(timeFactory.now())
                        .build())
                .flatMap(repository::save)
                .map(this::convertToEntity)
                .map(scheduleUpdated -> StatusResponse.<Campaign>builder()
                        .before(t.getT2().toBuilder().schedules(List.of(t.getT1())).build())
                        .actual(t.getT2().toBuilder().schedules(List.of(scheduleUpdated)).build())
                        .description("Actualizacion exitosa")
                        .build())
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
}
