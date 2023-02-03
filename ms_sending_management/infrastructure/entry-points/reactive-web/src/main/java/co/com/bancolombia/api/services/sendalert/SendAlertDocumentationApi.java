package co.com.bancolombia.api.services.sendalert;

import co.com.bancolombia.api.dto.AlertDTO;
import co.com.bancolombia.model.error.Error;
import co.com.bancolombia.model.message.Message;
import org.springdoc.core.fn.builders.operation.Builder;

import java.util.function.Consumer;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;

public class SendAlertDocumentationApi {

    private static final String TAG = "Send Alert";
    private static final String ERROR = "Error";
    private static final String SUCCESSFUL = "successful";

    protected Consumer<Builder> send() {
        return ops -> ops.tag(TAG)
                .operationId("SendAlert").summary(TAG)
                .description("Service to Send Alert").tags(new String[]{TAG})
                .requestBody(requestBodyBuilder().required(true).implementation(AlertDTO.class))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL).implementation(Boolean.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }


}
