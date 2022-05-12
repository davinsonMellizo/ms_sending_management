package co.com.bancolombia.usecase.campaign;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.campaign.Campaign;
import co.com.bancolombia.model.campaign.gateways.CampaignGateway;
import co.com.bancolombia.model.response.StatusResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CampaignUseCaseTest {

    @InjectMocks
    private CampaignUseCase useCase;

    @Mock
    private CampaignGateway campaignGateway;

    private final Campaign campaign = new Campaign();

    @BeforeEach
    private void init() {
        campaign.setId(1);
    }

    @Test
    void findAll() {
        when(campaignGateway.findAll())
                .thenReturn(Mono.just(List.of(campaign)));
        StepVerifier.create(useCase.findAllCampaign())
                .consumeNextWith(providers -> assertEquals(1, providers.size()))
                .verifyComplete();
        verify(campaignGateway).findAll();

    }

    @Test
    void findCampaignById() {
        when(campaignGateway.findCampaignById(any()))
                .thenReturn(Mono.just(campaign));
        StepVerifier.create(useCase.findCampaignById(campaign))
                .expectNextCount(1)
                .verifyComplete();
        verify(campaignGateway).findCampaignById(any());
    }

    @Test
    void saveCampaign() {
        when(campaignGateway.saveCampaign(any()))
                .thenReturn(Mono.just(campaign));
        StepVerifier.create(useCase.saveCampaign(campaign))
                .assertNext(response -> response.getId().equals(campaign.getId()))
                .verifyComplete();
        verify(campaignGateway).saveCampaign(any());
    }

    @Test
    void updateCampaign() {
        when(campaignGateway.updateCampaign(any()))
                .thenReturn(Mono.just(StatusResponse.<Campaign>builder()
                        .actual(campaign).before(campaign).build()));
        StepVerifier.create(useCase.updateCampaign(campaign))
                .assertNext(response -> response.getActual().getId().equals(campaign.getId()))
                .verifyComplete();
        verify(campaignGateway).updateCampaign(any());
    }

    @Test
    void deleteCampaign() {
        when(campaignGateway.findCampaignById(any()))
                .thenReturn(Mono.just(campaign));
        when(campaignGateway.deleteCampaignById(any()))
                .thenReturn(Mono.just(campaign.getId().toString()));
        StepVerifier.create(useCase.deleteCampaignById(campaign))
                .expectNextCount(1)
                .verifyComplete();
        verify(campaignGateway).deleteCampaignById(any());
    }

    @Test
    void updateCampaignWithException() {
        when(campaignGateway.updateCampaign(any()))
                .thenReturn(Mono.empty());
        useCase.updateCampaign(campaign)
                .as(StepVerifier::create)
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void deleteCampaignWithException() {
        when(campaignGateway.findCampaignById(any()))
                .thenReturn(Mono.empty());
        useCase.deleteCampaignById(campaign)
                .as(StepVerifier::create)
                .expectError(BusinessException.class)
                .verify();
    }
}
