package co.com.bancolombia.api.services.campaign;

import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.commons.util.ParamsUtil;
import co.com.bancolombia.api.commons.validators.groups.OnCreate;
import co.com.bancolombia.api.commons.validators.groups.OnDelete;
import co.com.bancolombia.api.commons.validators.groups.OnUpdate;
import co.com.bancolombia.api.dto.CampaignDTO;
import co.com.bancolombia.api.dto.ResponseDTO;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.usecase.campaign.CampaignUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.BODY_MISSING_ERROR;

@Component
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class CampaignHandler {

    private final CampaignUseCase useCase;
    private final ValidatorHandler validatorHandler;

    public Mono<ServerResponse> findCampaign(ServerRequest serverRequest) {
        return ParamsUtil.getCampaignParams(serverRequest)
                .flatMap(CampaignDTO::toModel)
                .flatMap(useCase::findCampaignById)
                .map(campaigns -> ResponseDTO.success(campaigns, serverRequest))
                .flatMap(ResponseDTO::responseOk);
    }

    public Mono<ServerResponse> findAllCampaign(ServerRequest serverRequest) {
        return useCase.findAllCampaign()
                .map(campaigns -> ResponseDTO.success(campaigns, serverRequest))
                .flatMap(ResponseDTO::responseOk);
    }

    public Mono<ServerResponse> saveCampaign(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CampaignDTO.class)
                .switchIfEmpty(Mono.error(new TechnicalException(BODY_MISSING_ERROR)))
                .doOnNext(campaignDTO -> validatorHandler.validateObject(campaignDTO, OnCreate.class))
                .flatMap(CampaignDTO::toModel)
                .flatMap(useCase::saveCampaign)
                .map(campaign -> ResponseDTO.success(campaign, serverRequest))
                .flatMap(ResponseDTO::responseOk);
    }

    public Mono<ServerResponse> updateCampaign(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CampaignDTO.class)
                .switchIfEmpty(Mono.error(new TechnicalException(BODY_MISSING_ERROR)))
                .doOnNext(campaignDTO -> validatorHandler.validateObject(campaignDTO, OnUpdate.class))
                .flatMap(CampaignDTO::toModel)
                .flatMap(useCase::updateCampaign)
                .map(campaign -> ResponseDTO.success(campaign, serverRequest))
                .flatMap(ResponseDTO::responseOk);
    }

    public Mono<ServerResponse> deleteCampaign(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CampaignDTO.class)
                .switchIfEmpty(Mono.error(new TechnicalException(BODY_MISSING_ERROR)))
                .doOnNext(campaignDTO -> validatorHandler.validateObject(campaignDTO, OnDelete.class))
                .flatMap(CampaignDTO::toModel)
                .flatMap(useCase::deleteCampaignById)
                .map(campaign -> ResponseDTO.success(campaign, serverRequest))
                .flatMap(ResponseDTO::responseOk);
    }

    public Mono<ServerResponse> deleteTrigger(ServerRequest serverRequest) {
        return ParamsUtil.getTriggerParams(serverRequest)
                .flatMap(useCase::deleteTrigger)
                .map(campaigns -> ResponseDTO.success(campaigns, serverRequest))
                .flatMap(ResponseDTO::responseOk);
    }
}
