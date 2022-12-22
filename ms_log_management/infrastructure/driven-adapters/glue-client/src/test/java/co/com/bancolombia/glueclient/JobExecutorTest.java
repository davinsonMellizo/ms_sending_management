package co.com.bancolombia.glueclient;

import co.com.bancolombia.commons.enums.TechnicalExceptionEnum;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.glueclient.config.GlueEtlProperties;
import co.com.bancolombia.model.log.Filters;
import com.amazonaws.services.glue.AWSGlue;
import com.amazonaws.services.glue.model.StartJobRunRequest;
import com.amazonaws.services.glue.model.StartJobRunResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;


import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobExecutorTest {

    @InjectMocks
    private JobExecutor jobExecutor;

    @Mock
    private AWSGlue glueClient;
    @Mock
    private GlueEtlProperties properties;

    @Captor
    private ArgumentCaptor<StartJobRunRequest> glueJobCaptor;

    @BeforeEach
    void init(){
        when(properties.getJobName()).thenReturn("job");
    }

    @Test
    void shouldUseGlueClientToExecuteJobWithSdkError() {
        when(glueClient.startJobRun(any())).thenThrow(new TechnicalException(new Throwable(),
                TechnicalExceptionEnum.ERROR_GLUE_ETL_AWS_SDK));
        StepVerifier.create(jobExecutor.retrieveLogsS3(createHistoricalFilterWithAllFields()))
                .expectErrorMatches(throwable -> throwable instanceof TechnicalException
                        && ((TechnicalException)throwable).getException()
                        .equals(TechnicalExceptionEnum.ERROR_GLUE_ETL_AWS_SDK))
                .verify();
    }

    @Test
    void shouldFailWithIllegalArgumentWithNullInitialDate() {
        Filters historicalFilter = Filters.builder().transactionIdentifier("request_id").endDate(LocalDateTime.now()).build();
        StepVerifier.create(jobExecutor.retrieveLogsS3(historicalFilter))
                .expectErrorMatches(throwable -> throwable instanceof TechnicalException
                        && ((TechnicalException)throwable).getException()
                        .equals(TechnicalExceptionEnum.ERROR_GLUE_ETL_MISSING_MANDATORY_ARGUMENTS))
                .verify();
    }

    @Test
    void shouldFailWithIllegalArgumentWithNullFinalDate() {
        Filters historicalFilter = Filters.builder().startDate(LocalDateTime.now().minusDays(1)).transactionIdentifier("request_id").build();
        StepVerifier.create(jobExecutor.retrieveLogsS3(historicalFilter))
                .expectErrorMatches(throwable -> throwable instanceof TechnicalException
                        && ((TechnicalException)throwable).getException()
                        .equals(TechnicalExceptionEnum.ERROR_GLUE_ETL_MISSING_MANDATORY_ARGUMENTS))
                .verify();
    }

    @Test
    void shouldUseGlueClientToExecuteJobSuccessfullyWithAllFields() {
        when(glueClient.startJobRun(any(StartJobRunRequest.class))).thenReturn(createSuccessfulResponse());
        StepVerifier.create(jobExecutor.retrieveLogsS3(createHistoricalFilterWithAllFields()))
                .expectNext("job-id")
                .verifyComplete();
        verify(glueClient, times(1)).startJobRun(glueJobCaptor.capture());
        StartJobRunRequest value = glueJobCaptor.getValue();
        assertEquals("request_id", value.getArguments().get("--transaction_identifier"));

    }

    private Filters createHistoricalFilterWithAllFields() {
        return Filters.builder()
                .startDate(LocalDateTime.now().minusDays(1))
                .endDate(LocalDateTime.now())
                .transactionIdentifier("request_id")
                .build();
    }

    private StartJobRunResult createSuccessfulResponse() {
        StartJobRunResult startJobRunResult = new StartJobRunResult();
        startJobRunResult.setJobRunId("job-id");
        return startJobRunResult;
    }
}
