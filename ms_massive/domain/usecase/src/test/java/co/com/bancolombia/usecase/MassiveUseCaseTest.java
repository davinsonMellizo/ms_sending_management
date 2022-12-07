package co.com.bancolombia.usecase;

import co.com.bancolombia.model.campaign.Campaign;
import co.com.bancolombia.model.campaign.gateways.CampaignGateway;
import co.com.bancolombia.model.campaign.gateways.CampaignGlueGateway;
import co.com.bancolombia.model.massive.Massive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MassiveUseCaseTest {

    @InjectMocks
    private MassiveUseCase useCase;

    @Mock
    private CampaignGateway campaignGateway;

    @Mock
    private CampaignGlueGateway glueGateway;

    private final Campaign campaign = new Campaign();

    @BeforeEach
    public void init() {
        campaign.setIdCampaign("1");
        campaign.setIdConsumer("SVP");
        campaign.setState("1");
    }


    @Test
    void activateCampaign() {
        Campaign campaignInactive = new Campaign();
        campaignInactive.setIdCampaign(campaign.getIdCampaign());
        campaignInactive.setIdConsumer(campaign.getIdConsumer());
        campaignInactive.setState("0");

        when(campaignGateway.findCampaignById(any()))
                .thenReturn(Mono.just(campaign));

        when(glueGateway.startTrigger(any()))
                .thenReturn(Mono.just("tgr_15_SVP_41"));

        StepVerifier.create(useCase.sendCampaign(Massive.builder()
                        .idCampaign(campaign.getIdCampaign())
                        .idConsumer(campaign.getIdConsumer())
                        .build()))

                .assertNext(response -> assertEquals(response.getTriggerName(), "tgr_15_SVP_41"))
                .verifyComplete();
        verify(campaignGateway).findCampaignById(any());
    }


}
