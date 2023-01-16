package co.com.bancolombia.campaign;

import co.com.bancolombia.campaign.data.CampaignData;
import co.com.bancolombia.campaign.data.CampaignMapper;
import co.com.bancolombia.commons.enums.ScheduleType;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.drivenadapters.TimeFactory;
import co.com.bancolombia.model.campaign.Campaign;
import co.com.bancolombia.model.schedule.Schedule;
import co.com.bancolombia.model.schedule.gateways.ScheduleGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class CampaignRepositoryImplExceptionTest {

    @InjectMocks
    private CampaignRepositoryImplement repositoryImpl;

    @Mock
    private ScheduleGateway scheduleGateway;

    @Mock
    private CampaignRepository repository;

    @Mock
    private TimeFactory timeFactory;

    @Spy
    private CampaignMapper mapper = Mappers.getMapper(CampaignMapper.class);

    private final Campaign campaign = new Campaign();
    private final Schedule schedule = new Schedule();
    private final CampaignData campaignData = new CampaignData();
    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final LocalDate DATE_NOW = LocalDate.now();
    private static final LocalTime TIME_NOW = LocalTime.now();


    @BeforeEach
    void init() {
        campaign.setIdCampaign("1");
        campaign.setIdConsumer("ALM");
        campaign.setProvider("{\"idProvider\":\"INA\",\"channelType\":\"PUSH\"}");
        campaign.setIdRemitter(0);
        campaign.setSourcePath("source_path");
        campaign.setAttachment(false);
        campaign.setState("1");
        campaign.setCreatedDate(NOW);
        campaign.setCreationUser("lugomez");

        schedule.setIdCampaign(campaign.getIdCampaign());
        schedule.setIdConsumer(campaign.getIdConsumer());
        schedule.setScheduleType(ScheduleType.DAILY);
        schedule.setStartDate(DATE_NOW);
        schedule.setStartTime(TIME_NOW);

        campaign.setSchedules(List.of(schedule));

        campaignData.setIdCampaign(campaign.getIdCampaign());
        campaignData.setIdConsumer(campaign.getIdConsumer());
    }

    @Test
    void findCampaignByIdWithException() {
        when(repository.findCampaign(anyString(), anyString()))
                .thenReturn(Mono.error(RuntimeException::new));
        repositoryImpl.findCampaignById(campaign)
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }

    @Test
    void saveCampaignWithException() {
        when(repository.save(any()))
                .thenReturn(Mono.error(RuntimeException::new));
        repositoryImpl.saveCampaign(campaign)
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }

    @Test
    void deleteCampaignWithException() {
        when(repository.deleteCampaign(any()))
                .thenReturn(Mono.error(RuntimeException::new));
        repositoryImpl.deleteCampaignById(campaign)
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }

    @Test
    void updateCampaignWithException() {
        when(repository.findCampaign(anyString(), anyString()))
                .thenReturn(Mono.just(campaignData));
        repositoryImpl.updateCampaign(campaign)
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }

}
