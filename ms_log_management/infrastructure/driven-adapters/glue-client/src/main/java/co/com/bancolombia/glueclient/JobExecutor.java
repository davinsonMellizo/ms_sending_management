package co.com.bancolombia.glueclient;

import co.com.bancolombia.commons.enums.TechnicalExceptionEnum;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.glueclient.config.GlueEtlProperties;
import co.com.bancolombia.model.log.Filters;
import co.com.bancolombia.model.log.gateways.RetrieveLogsGateway;
import com.amazonaws.services.glue.AWSGlue;
import com.amazonaws.services.glue.model.StartJobRunRequest;
import com.amazonaws.services.glue.model.StartJobRunResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JobExecutor implements RetrieveLogsGateway {

    private final AWSGlue glueClient;
    private final GlueEtlProperties properties;

    @Override
    public Mono<String> retrieveLogsS3(Filters filters) {
        return buildJobRequest(filters, properties.getJobName())
                .map(glueClient::startJobRun)
                .map(StartJobRunResult::getJobRunId)
                .onErrorMap(exception -> {
                    if (exception.getClass().equals(IllegalArgumentException.class)) {
                        return new TechnicalException(exception,
                                TechnicalExceptionEnum.ERROR_GLUE_ETL_MISSING_MANDATORY_ARGUMENTS);
                    }
                    return new TechnicalException(exception, TechnicalExceptionEnum.ERROR_GLUE_ETL_AWS_SDK);
                });
    }

    private Mono<StartJobRunRequest> buildJobRequest(Filters filter, String jobName) {

        Optional<String> validationMessage = validateHistoricalFilter(filter);
        if (validationMessage.isPresent()) {
            return Mono.error(new IllegalArgumentException(validationMessage.get()));
        }

        var startJobRunRequest = fillMandatoryArguments(filter, jobName);
        return Mono.just(startJobRunRequest);
    }

    private StartJobRunRequest fillMandatoryArguments(Filters filter, String jobName) {
        var startJobRunRequest = new StartJobRunRequest();
        startJobRunRequest.setJobName(jobName);
        startJobRunRequest.addArgumentsEntry("--transaction_identifier", filter.getTransactionIdentifier());
        startJobRunRequest.addArgumentsEntry("--document_type", validateEmpty(filter.getDocumentType()));
        startJobRunRequest.addArgumentsEntry("--document_number", validateEmpty(filter.getDocumentNumber()));
        startJobRunRequest.addArgumentsEntry("--start_date", filter.getStartDate().toString());
        startJobRunRequest.addArgumentsEntry("--end_date", filter.getEndDate().toString());
        startJobRunRequest.addArgumentsEntry("--contact",  validateEmpty(filter.getContact()));
        startJobRunRequest.addArgumentsEntry("--consumer", validateEmpty(filter.getConsumer()));
        startJobRunRequest.addArgumentsEntry("--provider", validateEmpty(filter.getProvider()));
        return startJobRunRequest;
    }

    private Optional<String> validateHistoricalFilter(Filters filter) {
        String message;
       if(filter.getStartDate() == null){
           message = "start date is null";
       }
       else if (filter.getEndDate()==null) {
           message = "end date is null";
       }
        else if (filter.getStartDate().isAfter(filter.getEndDate())) {
            message = "start date must be less than end date";
        }
        else {
            return Optional.empty();
        }
        return Optional.of(message);
    }

    private String validateEmpty(String value){
        return value.isEmpty()?" ": value;
    }
}