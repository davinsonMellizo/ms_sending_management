package co.com.bancolombia.api.services.contact;

import co.com.bancolombia.api.dto.ResponseContactsDTO;
import co.com.bancolombia.api.commons.handlers.Error;
import org.springdoc.core.fn.builders.parameter.Builder;

import java.util.function.Consumer;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;
import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;

public class ClientDocumentationApi {

    private static final String TAG = "Clients";
    private static final String ERROR = "Error";
    private static final String SUCCESSFUL = "successful";

    protected Consumer<org.springdoc.core.fn.builders.operation.Builder> find() {
        return ops -> ops.tag(TAG)
                .operationId("findClients").summary("Find clients")
                .description("Find Client by client").tags(new String[]{TAG})
                .parameter(createHeader(Long.class, "document-number", "Client Document Number"))
                .parameter(createHeader(String.class, "document-type", "Client Document Type"))
                .parameter(headerNoRequired(String.class, "consumer", "Code consumer"))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL)
                        .implementation(ResponseContactsDTO.class))
                .response(responseBuilder().responseCode("400").description("Bad Request").implementation(String.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    private <T> Builder createHeader(Class<T> clazz, String name, String description) {
        return parameterBuilder().in(HEADER).implementation(clazz).required(true).name(name).description(description);
    }

    private <T> Builder headerNoRequired(Class<T> clazz, String name, String description) {
        return parameterBuilder().in(HEADER).implementation(clazz).name(name).description(description);
    }
}
