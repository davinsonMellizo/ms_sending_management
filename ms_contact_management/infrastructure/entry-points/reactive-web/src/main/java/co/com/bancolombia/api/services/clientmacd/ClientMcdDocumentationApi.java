package co.com.bancolombia.api.services.clientmacd;

import co.com.bancolombia.api.dto.EnrolDTO;
import co.com.bancolombia.api.commons.handlers.Error;
import co.com.bancolombia.model.response.StatusResponse;
import org.springdoc.core.fn.builders.operation.Builder;

import java.util.function.Consumer;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;

public class ClientMcdDocumentationApi {

    private static final String TAG = "Client Macd";
    private static final String ERROR = "Error";
    private static final String SUCCESSFUL = "successful";
    private static final String STATUS_500 = "500";
    private static final String STATUS_200 = "200";

    protected Consumer<Builder> update() {
        return ops -> ops.tag(TAG)
                .operationId("updateClient").summary("Update Client")
                .description("Update client Client ").tags(new String[]{TAG})
                .requestBody(requestBodyBuilder().description("Client to Update").required(true)
                        .implementation(EnrolDTO.class))
                .response(responseBuilder().responseCode(STATUS_200).description(SUCCESSFUL)
                        .implementation(StatusResponse.class))
                .response(responseBuilder().responseCode(STATUS_500).description(ERROR).implementation(Error.class));
    }
}
