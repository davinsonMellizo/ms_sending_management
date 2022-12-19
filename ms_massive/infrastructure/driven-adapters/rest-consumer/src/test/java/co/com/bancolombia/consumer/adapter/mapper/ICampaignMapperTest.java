package co.com.bancolombia.consumer.adapter.mapper;

import co.com.bancolombia.consumer.adapter.campaign.mapper.ICampaignMapperImpl;
import co.com.bancolombia.consumer.adapter.campaign.model.SuccessCampaign;
import co.com.bancolombia.model.campaign.Campaign;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;


@ExtendWith(MockitoExtension.class)
class ICampaignMapperTest {

    private ICampaignMapperImpl iCampaignMapper;

    @BeforeEach
    void setUp() {
        iCampaignMapper = new ICampaignMapperImpl();
    }


    @Test
    void successToModel() {
        SuccessCampaign successCampaign = SuccessCampaign
                .builder()
                .data(SuccessCampaign.Data
                        .builder()
                        .idCampaign("15")
                        .idConsumer("SVP")
                        .schedules(List.of(
                                SuccessCampaign
                                        .ScheduleResponse
                                        .builder()
                                        .idCampaign("15")
                                        .build()
                        ))
                        .build())
                .build();

        Campaign campaign = iCampaignMapper.toModel(successCampaign.getData());
        Assertions.assertThat(campaign.getSchedules()).isNotNull();
        Assertions.assertThat(campaign).hasFieldOrPropertyWithValue("idCampaign", "15");
    }

    @Test
    void nullToModel() {
        Assertions.assertThat(iCampaignMapper.toModel(null)).isNull();
    }
}
