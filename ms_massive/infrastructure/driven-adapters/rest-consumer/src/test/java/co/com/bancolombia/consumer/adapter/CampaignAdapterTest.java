package co.com.bancolombia.consumer.adapter;

import co.com.bancolombia.commons.enums.BusinessExceptionEnum;
import co.com.bancolombia.commons.enums.TechnicalExceptionEnum;
import co.com.bancolombia.commons.exception.BusinessException;
import co.com.bancolombia.commons.exception.TechnicalException;
import co.com.bancolombia.consumer.RestConsumer;
import co.com.bancolombia.consumer.adapter.campaign.mapper.ICampaignMapper;
import co.com.bancolombia.consumer.adapter.campaign.model.SuccessCampaign;
import co.com.bancolombia.consumer.config.RestConsumerProperties;
import co.com.bancolombia.consumer.model.Error;
import co.com.bancolombia.model.campaign.Campaign;
import co.com.bancolombia.model.massive.Massive;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CampaignAdapterTest {
    @Mock
    private RestConsumer restConsumer;
    @Mock
    private ICampaignMapper campaignMapper;
    @Mock
    private RestConsumerProperties properties;
    @InjectMocks
    private CampaignAdapter campaignAdapter;
    @Spy
    private RestConsumerProperties.Resources resources = new RestConsumerProperties.Resources();

    private Massive massive;

    @BeforeEach
    void setUp() {
        massive = Massive.builder()
                .idCampaign("15")
                .idConsumer("SVP")
                .build();
        resources.setEndpointCampaign("/campaign");
        when(properties.getResources()).thenReturn(resources);
    }

    @Test
    void successFind() {
        when(restConsumer.get(anyString(), any(), any(), any()))
                .thenReturn(Mono.just(SuccessCampaign.builder().build()));

        when(campaignMapper.toModel(any())).thenReturn(Campaign.builder().build());

        StepVerifier.create(campaignAdapter.findCampaignById(massive))
                .assertNext(campaign -> Assertions.assertThat(campaign).isInstanceOf(Campaign.class))
                .verifyComplete();
    }

    @Test
    void findCampaignByIdWithRequestingException() {
        when(restConsumer.get(anyString(), any(), any(), any()))
                .thenReturn(Mono.error(new TechnicalException(TechnicalExceptionEnum.TECHNICAL_ERROR_REQUESTING_CAMPAIGN)));

        StepVerifier.create(campaignAdapter.findCampaignById(massive))
                .expectErrorMatches(throwable -> throwable instanceof TechnicalException &&
                        ((TechnicalException) throwable).getException()
                                .equals(TechnicalExceptionEnum.TECHNICAL_ERROR_REQUESTING_CAMPAIGN)
                ).verify();
    }

    @Test
    void findCampaignByIdNotFound() {
        when(restConsumer.get(anyString(), any(), any(), any())).thenReturn(Mono.error(Error.builder().build()));

        StepVerifier.create(campaignAdapter.findCampaignById(massive))
                .expectErrorMatches(throwable -> throwable instanceof BusinessException &&
                        ((BusinessException) throwable).getException()
                                .equals(BusinessExceptionEnum.BUSINESS_CAMPAIGN_NOT_FOUND)
                ).verify();
    }
}
