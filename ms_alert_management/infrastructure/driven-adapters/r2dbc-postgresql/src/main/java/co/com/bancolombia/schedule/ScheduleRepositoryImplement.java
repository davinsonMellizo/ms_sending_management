package co.com.bancolombia.schedule;

import co.com.bancolombia.AdapterOperations;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.drivenadapters.TimeFactory;
import co.com.bancolombia.model.campaign.Campaign;
import co.com.bancolombia.model.schedule.Schedule;
import co.com.bancolombia.model.schedule.gateways.ScheduleGateway;
import co.com.bancolombia.schedule.data.ScheduleData;
import co.com.bancolombia.schedule.data.ScheduleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.*;

@Repository
public class ScheduleRepositoryImplement extends AdapterOperations<Schedule, ScheduleData, Integer, ScheduleRepository>
        implements ScheduleGateway {

    @Autowired
    public ScheduleRepositoryImplement(ScheduleRepository repository, ScheduleMapper mapper) {
        super(repository, mapper::toData, mapper::toEntity);
    }

    @Autowired
    private TimeFactory timeFactory;

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
                                .isNew(true)
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
                                .isNew(false)
                                .idCampaign(campaign.getIdCampaign())
                                .idConsumer(campaign.getIdConsumer())
                                .creationUser(campaign.getCreationUser())
                                .createdDate(campaign.getCreatedDate())
                                .build())
                        .collect(Collectors.toList()))
                .flatMap(scheduleList -> repository.saveAll(scheduleList)
                        .map(this::convertToEntity)
                        .collectList()
                        .map(campaign::withSchedules))
                .onErrorMap(e -> new TechnicalException(e, SAVE_CAMPAIGN_ERROR));
    }

    @Override
    public Mono<String> deleteSchedulesByCampaign(Campaign campaign) {
        return repository.deleteSchedulesByCampaign(campaign.getIdCampaign(), campaign.getIdConsumer())
                .onErrorMap(e -> new TechnicalException(e, DELETE_CAMPAIGN_ERROR))
                .thenReturn(campaign.getIdCampaign());
    }
}
