package co.com.bancolombia.api.service;

import java.util.function.Consumer;

import org.springdoc.core.fn.builders.operation.Builder;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;

import co.com.bancolombia.model.error.Error;
import co.com.bancolombia.model.massive.Massive;

public class MassiveDocumentationApi {

    private static final String TAG = "Massive";
    private static final String ERROR = "Error";
    private static final String SUCCESSFUL = "successful";

    protected Consumer<Builder> sendCampaignOnDemand() {
        return ops -> ops.tag(TAG)
                .operationId("SendCampaignOnDemand").summary("Send Campaign on demand")
                .description("Send a new massive Email, SMS and Push campaign on demand").tags(new String[]{TAG})
                .requestBody(requestBodyBuilder().description("Campaign to send").required(true)
                        .implementation(Massive.class))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL)
                        .implementation(Massive.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

}
