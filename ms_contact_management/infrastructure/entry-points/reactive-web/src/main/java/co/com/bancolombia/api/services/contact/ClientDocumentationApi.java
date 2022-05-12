package co.com.bancolombia.api.services.contact;

import co.com.bancolombia.api.dto.ResponseContactsDTO;
import co.com.bancolombia.model.error.Error;
import org.springdoc.core.fn.builders.operation.Builder;

import java.util.function.Consumer;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;
import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;

public class ClientDocumentationApi {

    private final String TAG = "Clients";
    private final String ERROR = "Error";
    private final String SUCCESSFUL = "successful";

    protected Consumer<Builder> find() {
        return ops -> ops.tag(TAG)
                .operationId("findClients").summary("Find clients")
                .description("Find Client by client").tags(new String[]{TAG})
                .parameter(createHeader(Long.class, "document-number", "Client Document Number"))
                .parameter(createHeader(String.class, "document-type", "Client Document Type"))
                .parameter(headerNoRequired(String.class, "consumer", "Code consumer"))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL).implementation(ResponseContactsDTO.class))
                .response(responseBuilder().responseCode("400").description("Bad Request").implementation(String.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    private <T> org.springdoc.core.fn.builders.parameter.Builder createHeader(Class<T> clazz, String name, String description) {
        return parameterBuilder().in(HEADER).implementation(clazz).required(true).name(name).description(description);
    }

    private <T> org.springdoc.core.fn.builders.parameter.Builder headerNoRequired(Class<T> clazz, String name, String description) {
        return parameterBuilder().in(HEADER).implementation(clazz).name(name).description(description);
    }
}
