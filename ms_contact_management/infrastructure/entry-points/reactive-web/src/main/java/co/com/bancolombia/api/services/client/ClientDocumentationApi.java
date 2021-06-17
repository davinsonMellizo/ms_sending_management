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

    private final String tag = "Clients";
    private final String error = "Error";
    private final String successful = "successful";

    protected Consumer<Builder> saveClientAPI() {
        return ops -> ops.tag(tag)
                .operationId("SaveClient").summary("Save Client")
                .description("save a Client").tags(new String[]{tag})
                .requestBody(requestBodyBuilder().description("Client to create").required(true).implementation(ClientDTO.class))
                .response(responseBuilder().responseCode("200").description(successful).implementation(Client.class))
                .response(responseBuilder().responseCode("500").description(error).implementation(Error.class));
    }

    protected Consumer<Builder> findClientAPI() {
        return ops -> ops.tag(tag)
                .operationId("findClient").summary("Find Clients")
                .description("Find Client by number and type document").tags(new String[]{tag})
                .parameter(createHeader(Long.class, "document-number", "Client Document Number"))
                .parameter(createHeader(Integer.class, "document-type", "Client Document Type"))
                .response(responseBuilder().responseCode("200").description(successful).implementation(Client.class))
                .response(responseBuilder().responseCode("500").description(error).implementation(Error.class));
    }

    protected Consumer<Builder> updateClientAPI() {
        return ops -> ops.tag(tag)
                .operationId("updateClient").summary("Update Client")
                .description("Update client Client ").tags(new String[]{tag})
                .requestBody(requestBodyBuilder().description("Client to Update").required(true).implementation(ClientDTO.class))
                .response(responseBuilder().responseCode("200").description(successful).implementation(StatusResponse.class))
                .response(responseBuilder().responseCode("500").description(error).implementation(Error.class));
    }

    protected Consumer<Builder> deleteClientAPI() {
        return ops -> ops.tag(tag)
                .operationId("deleteClient").summary("Delete Client")
                .description("Delete a client Client").tags(new String[]{tag})
                .parameter(createHeader(Long.class, "document-number", "Client Document Number"))
                .parameter(createHeader(Integer.class, "document-type", "Client Document Type"))
                .response(responseBuilder().responseCode("200").description(successful).implementation(String.class))
                .response(responseBuilder().responseCode("500").description(error).implementation(Error.class));
    }

    private <T> org.springdoc.core.fn.builders.parameter.Builder createHeader(Class<T> clazz, String name, String description) {
        return parameterBuilder().in(HEADER).implementation(clazz).required(true).name(name).description(description);
    }
}
