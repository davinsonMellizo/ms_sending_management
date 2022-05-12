package co.com.bancolombia.alertclient;

import co.com.bancolombia.alertclient.data.AlertClientMapper;
import co.com.bancolombia.drivenadapters.TimeFactory;
import co.com.bancolombia.model.alertclient.AlertClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class AlertClientRepositoryImplExceptionTest {

    @InjectMocks
    private AlertClientRepositoryImplement repositoryImpl;
    @Mock
    private AlertClientRepository repository;
    @Spy
    private AlertClientMapper mapper = Mappers.getMapper(AlertClientMapper.class);
    private static final LocalDateTime NOW = LocalDateTime.now();
    @Mock
    private TimeFactory timeFactory;

    private final AlertClient alertClient = new AlertClient();

    @BeforeEach
    public void init() {
        alertClient.setIdAlert("HGD");
        alertClient.setDocumentNumber(1L);
        alertClient.setDocumentType(0);
        alertClient.setNumberOperations(5);
        alertClient.setAmountEnable(9L);
        alertClient.setAccumulatedOperations(8);
        alertClient.setAccumulatedAmount(1L);
        alertClient.setAssociationOrigin("ac");
        alertClient.setCreationUser("user");
    }

    @Test
    void test() {

    }



}