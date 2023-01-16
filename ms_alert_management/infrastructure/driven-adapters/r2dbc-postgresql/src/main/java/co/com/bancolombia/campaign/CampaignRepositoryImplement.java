package co.com.bancolombia.campaign;

import co.com.bancolombia.AdapterOperations;
import co.com.bancolombia.campaign.data.CampaignData;
import co.com.bancolombia.campaign.data.CampaignMapper;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.drivenadapters.TimeFactory;
import co.com.bancolombia.model.campaign.Campaign;
import co.com.bancolombia.model.campaign.gateways.CampaignGateway;
import co.com.bancolombia.model.response.StatusResponse;
import co.com.bancolombia.model.schedule.gateways.ScheduleGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.DELETE_CAMPAIGN_ERROR;
import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.FIND_ALL_CAMPAIGN_ERROR;
import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.FIND_CAMPAIGN_BY_ID_ERROR;
import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.SAVE_CAMPAIGN_ERROR;
import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.UPDATE_CAMPAIGN_ERROR;

@Repository
public class CampaignRepositoryImplement
        extends AdapterOperations<Campaign, CampaignData, String, CampaignRepository>
        implements CampaignGateway {

    @Autowired
    public CampaignRepositoryImplement(CampaignRepository repository, CampaignMapper mapper) {
        super(repository, mapper::toData, mapper::toEntity);
    }

    @Autowired
    private TimeFactory timeFactory;

    @Autowired
    private ScheduleGateway scheduleGateway;

    @Override
    public Flux<Campaign> findAll() {
        return repository.findAll()
                .map(this::convertToEntity)
                .flatMap(scheduleGateway::findSchedulesByCampaign)
                .onErrorMap(e -> new TechnicalException(e, FIND_ALL_CAMPAIGN_ERROR));
    }

    @Override
    public Mono<Campaign> findCampaignById(Campaign campaign) {
        return doQuery(repository.findCampaign(campaign.getIdCampaign(), campaign.getIdConsumer()))
                .flatMap(scheduleGateway::findSchedulesByCampaign)
                .onErrorMap(e -> new TechnicalException(e, FIND_CAMPAIGN_BY_ID_ERROR));
    }

    @Override
    public Mono<Campaign> saveCampaign(Campaign campaign) {
        return Mono.just(campaign)
                .map(this::convertToData)
                .map(campaignData -> campaignData.toBuilder()
                        .createdDate(timeFactory.now())
                        .modifiedUser(null)
                        .build())
                .flatMap(repository::save)
                .map(this::convertToEntity)
                .flatMap(scheduleGateway::saveSchedulesByCampaign)
                .onErrorMap(e -> new TechnicalException(e, SAVE_CAMPAIGN_ERROR));
    }

    @Override
    public Mono<StatusResponse<Campaign>> updateCampaign(Campaign campaign) {
        return findCampaignById(campaign)
                .map(campaignFound -> StatusResponse.<Campaign>builder()
                        .before(campaignFound)
                        .actual(campaign)
                        .build())
                .flatMap(this::update);
    }

    private Mono<StatusResponse<Campaign>> update(StatusResponse<Campaign> response) {
        return Mono.just(response.getActual())
                .map(data -> data.toBuilder()
                        .createdDate(response.getBefore().getCreatedDate())
                        .creationUser(response.getBefore().getCreationUser())
                        .state(data.getState() != null ? data.getState() : response.getBefore().getState())
                        .modifiedDate(timeFactory.now())
                        .build())
                .flatMap(repository::updateCampaign)
                .map(this::convertToEntity)
                .map(campaignActual -> response.toBuilder().actual(
                        campaignActual.toBuilder().schedules(response.getBefore().getSchedules()).build()
                ).description("Actualizacion exitosa").build())
                .onErrorMap(e -> new TechnicalException(e, UPDATE_CAMPAIGN_ERROR));
    }

    @Override
    public Mono<Map<String, String>> deleteCampaignById(Campaign campaign) {
        return repository.deleteCampaign(campaign)
                .onErrorMap(e -> new TechnicalException(e, DELETE_CAMPAIGN_ERROR))
                .thenReturn(
                        Map.of("idCampaign", campaign.getIdCampaign(), "idConsumer", campaign.getIdConsumer())
                );
    }
}
