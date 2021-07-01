package co.com.bancolombia.api.services.client;

import co.com.bancolombia.api.dto.ClientDTO;
import co.com.bancolombia.model.client.Client;
import co.com.bancolombia.model.error.Error;
import co.com.bancolombia.model.response.StatusResponse;
import org.springdoc.core.fn.builders.operation.Builder;

import java.util.function.Consumer;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;
import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;

public class ClientDocumentationApi {

    private final String TAG = "Clients";
    private final String ERROR = "Error";
    private final String SUCCESSFUL = "successful";

    protected Consumer<Builder> save() {
        return ops -> ops.tag(TAG)
                .operationId("SaveClient").summary("Save Client")
                .description("save a Client").tags(new String[]{TAG})
                .requestBody(requestBodyBuilder().description("Client to create").required(true).implementation(ClientDTO.class))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL).implementation(Client.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    protected Consumer<Builder> find() {
        return ops -> ops.tag(TAG)
                .operationId("findClient").summary("Find Clients")
                .description("Find Client by number and type document").tags(new String[]{TAG})
                .parameter(createHeader(Long.class, "document-number", "Client Document Number"))
                .parameter(createHeader(Integer.class, "document-type", "Client Document Type"))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL).implementation(Client.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    protected Consumer<Builder> update() {
        return ops -> ops.tag(TAG)
                .operationId("updateClient").summary("Update Client")
                .description("Update client Client ").tags(new String[]{TAG})
                .requestBody(requestBodyBuilder().description("Client to Update").required(true).implementation(ClientDTO.class))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL).implementation(StatusResponse.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    protected Consumer<Builder> delete() {
        return ops -> ops.tag(TAG)
                .operationId("deleteClient").summary("Delete Client")
                .description("Delete a client Client").tags(new String[]{TAG})
                .parameter(createHeader(Long.class, "document-number", "Client Document Number"))
                .parameter(createHeader(Integer.class, "document-type", "Client Document Type"))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL).implementation(String.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    private <T> org.springdoc.core.fn.builders.parameter.Builder createHeader(Class<T> clazz, String name, String description) {
        return parameterBuilder().in(HEADER).implementation(clazz).required(true).name(name).description(description);
    }
}
