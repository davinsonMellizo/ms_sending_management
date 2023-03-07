package co.com.bancolombia.api;

import co.com.bancolombia.api.DTO.AlertDTO;
import co.com.bancolombia.model.error.Error;
import org.springdoc.core.fn.builders.operation.Builder;

import java.util.function.Consumer;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;



public class SendSmsDocumentationApi {

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
