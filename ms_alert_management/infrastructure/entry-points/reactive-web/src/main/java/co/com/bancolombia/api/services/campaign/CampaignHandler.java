package co.com.bancolombia.api.services.campaign;

import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.commons.util.ParamsUtil;
import co.com.bancolombia.api.commons.util.ResponseUtil;
import co.com.bancolombia.api.dto.CampaignDTO;
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
public class CampaignHandler {

    private final CampaignUseCase useCase;
    private final ValidatorHandler validatorHandler;

    public Mono<ServerResponse> findCampaign(ServerRequest serverRequest) {
        return ParamsUtil.getCampaignHeader(serverRequest)
                .flatMap(CampaignDTO::toModel)
                .flatMap(useCase::findCampaignById)
                .flatMap(ResponseUtil::responseOk);
    }

    public Mono<ServerResponse> findAllCampaign(ServerRequest serverRequest) {
        return useCase.findAllCampaign()
                .flatMap(ResponseUtil::responseOk);
    }

    public Mono<ServerResponse> saveCampaign(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CampaignDTO.class)
                .switchIfEmpty(Mono.error(new TechnicalException(BODY_MISSING_ERROR)))
                .doOnNext(validatorHandler::validateObject)
                .flatMap(CampaignDTO::toModel)
                .flatMap(useCase::saveCampaign)
                .flatMap(ResponseUtil::responseOk);
    }

    public Mono<ServerResponse> updateCampaign(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CampaignDTO.class)
                .switchIfEmpty(Mono.error(new TechnicalException(BODY_MISSING_ERROR)))
                .doOnNext(validatorHandler::validateObject)
                .flatMap(CampaignDTO::toModel)
                .flatMap(useCase::updateCampaign)
                .flatMap(ResponseUtil::responseOk);
    }

    public Mono<ServerResponse> deleteCampaign(ServerRequest serverRequest) {
        return ParamsUtil.getCampaignHeader(serverRequest)
                .flatMap(CampaignDTO::toModel)
                .flatMap(useCase::deleteCampaignById)
                .flatMap(ResponseUtil::responseOk);
    }
}
