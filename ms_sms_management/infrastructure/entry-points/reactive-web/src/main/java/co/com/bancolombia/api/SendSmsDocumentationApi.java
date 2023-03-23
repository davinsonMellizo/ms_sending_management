package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.AlertDTO;
import co.com.bancolombia.api.dto.ResponseDTO;
import co.com.bancolombia.model.error.Error;
import org.springdoc.core.fn.builders.operation.Builder;

import java.util.function.Consumer;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;



public class SendSmsDocumentationApi {

    private static final String TAG = "Send Sms";
    private static final String ERROR = "Error";
    private static final String SUCCESSFUL = "successful";

    protected Consumer<Builder> send() {
        return ops -> ops.tag(TAG)
                .operationId("SendSms").summary(TAG)
                .description("Service to Send SMS").tags(new String[]{TAG})
                .requestBody(requestBodyBuilder().required(true).implementation(AlertDTO.class))
                .response(responseBuilder().responseCode("B0004").description(SUCCESSFUL).implementation(ResponseDTO.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }
}
