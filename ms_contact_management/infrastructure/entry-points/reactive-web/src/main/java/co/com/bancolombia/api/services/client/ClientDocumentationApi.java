package co.com.bancolombia.api.services.client;

import co.com.bancolombia.api.dto.EnrolDTO;
import co.com.bancolombia.api.dto.IdentificationDTO;
import co.com.bancolombia.api.dto.ResponseContactsDTO;
import co.com.bancolombia.model.client.ResponseUpdateClient;
import co.com.bancolombia.model.error.Error;
import org.springdoc.core.fn.builders.operation.Builder;

import java.util.function.Consumer;

import static co.com.bancolombia.commons.enums.Header.CONSUMER_CODE;
import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;
import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;

public class ClientDocumentationApi {

    private final String TAG = "Clients";
    private final String ERROR = "Error";
    private final String SUCCESSFUL = "successful";
    private final String STATUS_500 = "500";
    private final String STATUS_200 = "200";
    private final String CONSUMER_CODE_DES = "Consumer Code";

    protected Consumer<Builder> save() {
        return ops -> ops.tag(TAG)
                .operationId("SaveClient").summary("Save Client")
                .description("save a Client").tags(new String[]{TAG})
                .requestBody(requestBodyBuilder().description("Client to create").required(true).implementation(EnrolDTO.class))
                .response(responseBuilder().responseCode(STATUS_200).description(SUCCESSFUL)
                        .implementation(ResponseUpdateClient.class))
                .response(responseBuilder().responseCode(STATUS_500).description(ERROR)
                        .implementation(Error.class));
    }

    protected Consumer<Builder> find() {
        return ops -> ops.tag(TAG)
                .operationId("findClients").summary("Find clients")
                .description("Find Client by client").tags(new String[]{TAG})
                .parameter(createHeader(Long.class, "document-number", "Client Document Number"))
                .parameter(createHeader(String.class, "document-type", "Client Document Type"))
                .parameter(headerNoRequired(String.class, "consumer", "Code consumer"))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL)
                        .implementation(ResponseContactsDTO.class))
                .response(responseBuilder().responseCode("400").description("Bad Request")
                        .implementation(String.class))
                .response(responseBuilder().responseCode("500").description(ERROR)
                        .implementation(Error.class));
    }

    protected Consumer<Builder> inactive() {
        return ops -> ops.tag(TAG)
                .operationId("inactiveClient").summary("Inactive Client")
                .description("Inactive Client by number and type document").tags(new String[]{TAG})
                .parameter(createHeader(String.class, CONSUMER_CODE, CONSUMER_CODE_DES))
                .requestBody(requestBodyBuilder().description("Customer identification").required(true)
                        .implementation(IdentificationDTO.class))
                .response(responseBuilder().responseCode(STATUS_200).description(SUCCESSFUL)
                        .implementation(ResponseUpdateClient.class))
                .response(responseBuilder().responseCode(STATUS_500).description(ERROR)
                        .implementation(Error.class));
    }

    protected Consumer<Builder> update() {
        return ops -> ops.tag(TAG)
                .operationId("updateClient").summary("Update Client")
                .description("Update client Client ").tags(new String[]{TAG})
                .requestBody(requestBodyBuilder().description("Client to Update").required(true)
                        .implementation(EnrolDTO.class))
                .response(responseBuilder().responseCode(STATUS_200).description(SUCCESSFUL)
                        .implementation(ResponseUpdateClient
                                .class))
                .response(responseBuilder().responseCode(STATUS_500).description(ERROR)
                        .implementation(Error.class));
    }

    private <T> org.springdoc.core.fn.builders.parameter.Builder createHeader(Class<T> clazz,
                                                                              String name, String description) {
        return parameterBuilder().in(HEADER).implementation(clazz).required(true)
                .name(name).description(description);
    }

    private <T> org.springdoc.core.fn.builders.parameter.Builder headerNoRequired(Class<T> clazz,
                                                                                  String name, String description) {
        return parameterBuilder().in(HEADER).implementation(clazz).name(name)
                .description(description);
    }
}
