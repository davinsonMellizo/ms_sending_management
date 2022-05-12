package co.com.bancolombia.api.services.clientmacd;

import co.com.bancolombia.api.dto.EnrolDTO;
import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.error.Error;
import co.com.bancolombia.model.response.StatusResponse;
import org.springdoc.core.fn.builders.operation.Builder;

import java.util.function.Consumer;

import static co.com.bancolombia.commons.enums.Header.DOCUMENT_NUMBER;
import static co.com.bancolombia.commons.enums.Header.DOCUMENT_TYPE;
import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;
import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;

public class ClientMcdDocumentationApi {

    private final String TAG = "Client Macd";
    private final String ERROR = "Error";
    private final String SUCCESSFUL = "successful";
    private final String STATUS_500 = "500";
    private final String STATUS_200 = "200";

    protected Consumer<Builder> update() {
        return ops -> ops.tag(TAG)
                .operationId("updateClient").summary("Update Client")
                .description("Update client Client ").tags(new String[]{TAG})
                .requestBody(requestBodyBuilder().description("Client to Update").required(true).implementation(EnrolDTO.class))
                .response(responseBuilder().responseCode(STATUS_200).description(SUCCESSFUL).implementation(StatusResponse.class))
                .response(responseBuilder().responseCode(STATUS_500).description(ERROR).implementation(Error.class));
    }
}
