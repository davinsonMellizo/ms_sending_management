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
import reactor.core.publisher.Mono;

import java.util.List;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.*;

@Repository
public class CampaignRepositoryImplement
        extends AdapterOperations<Campaign, CampaignData, Integer, CampaignRepository>
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
    public Mono<List<Campaign>> findAll() {
        return repository.findAll()
                .map(this::convertToEntity)
                .flatMap(campaign -> scheduleGateway.findSchedulesByCampaign(campaign))
                .collectList()
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
                        .isNew(true)
                        .createdDate(timeFactory.now())
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
                        .actual(campaign.toBuilder()
                                .id(campaignFound.getId())
                                .build())
                        .build())
                .flatMap(this::update);
    }

    private Mono<StatusResponse<Campaign>> update(StatusResponse<Campaign> response) {
        return Mono.just(response.getActual())
                .map(this::convertToData)
                .map(data -> data.toBuilder()
                        .isNew(false)
                        .createdDate(response.getBefore().getCreatedDate())
                        .build())
                .flatMap(repository::save)
                .map(this::convertToEntity)
                .flatMap(scheduleGateway::updateSchedulesByCampaign)
                .map(actual -> response.toBuilder().actual(actual).description("Actualizacion Exitosa").build())
                .onErrorMap(e -> new TechnicalException(e, UPDATE_CAMPAIGN_ERROR));
    }

    @Override
    public Mono<String> deleteCampaignById(Campaign campaign) {
        return scheduleGateway.deleteSchedulesByCampaign(campaign)
                .onErrorMap(e -> new TechnicalException(e, DELETE_CAMPAIGN_ERROR))
                .thenReturn(campaign.getIdCampaign())
                .flatMap(idCampaign -> repository.deleteCampaign(idCampaign, campaign.getIdConsumer()))
                .onErrorMap(e -> new TechnicalException(e, DELETE_CAMPAIGN_ERROR))
                .thenReturn(campaign.getIdCampaign());
    }
}
