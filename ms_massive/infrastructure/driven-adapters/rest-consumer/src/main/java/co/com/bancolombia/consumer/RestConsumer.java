package co.com.bancolombia.consumer;

import co.com.bancolombia.consumer.model.ErrorResponse;
import co.com.bancolombia.consumer.util.Util;
import co.com.bancolombia.model.campaign.Campaign;
import co.com.bancolombia.model.campaign.gateways.CampaignRepository;
import co.com.bancolombia.model.error.Error;
import co.com.bancolombia.model.massive.Massive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RestConsumer implements CampaignRepository {

    private final WebClient client;

    private Mono<Throwable> buildError(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(Error.class)
                .map(d -> ErrorResponse.builder()
                        .status(clientResponse.statusCode().value())
                        .data(d)
                        .build());
    }

    @Override
    public Mono<Campaign> findCampaignById(Massive massive) {
        return client
                .get()
                .uri(uri -> uri
                        .queryParams(Util.paramsCampaign(massive.getIdCampaign(), massive.getIdConsumer()))
                        .build())
                .retrieve()
                .onStatus(HttpStatus::isError, this::buildError)
                .bodyToMono(Campaign.class);
    }
}