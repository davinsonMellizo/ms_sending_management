package co.com.bancolombia.api.services.providerservice;

import co.com.bancolombia.api.dto.ProviderServiceDTO;
import co.com.bancolombia.model.error.Error;
import org.springdoc.core.fn.builders.operation.Builder;

import java.util.function.Consumer;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;
import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;
import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;

public class ProviderServiceDocumentationApi {

    private final static String TAG = "Alert";
    private final static String ERROR = "Error";
    private final static String SUCCESSFUL = "successful";

    protected Consumer<Builder> save() {
        return ops -> ops.tag(TAG)
                .operationId("associateProviderToService").summary("Associate provider to service")
                .description("Associate provider to service").tags(new String[]{TAG})
                .requestBody(requestBodyBuilder().description("Relation provider service to create")
                        .required(true).implementation(ProviderServiceDTO.class))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL)
                        .implementation(ProviderServiceDTO.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    protected Consumer<Builder> find() {
        return ops -> ops.tag(TAG)
                .operationId("findProviderService").summary("find all provider service relations")
                .description("Find all provider relations with service").tags(new String[]{TAG})
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL)
                        .implementationArray(ProviderServiceDTO.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    protected Consumer<Builder> delete() {
        return ops -> ops.tag(TAG)
                .operationId("deleteProviderService").summary("Delete provider association")
                .description("Delete provider association with service").tags(new String[]{TAG})
                .parameter(createQuery(String.class, "idProvider", "Provider identifier"))
                .parameter(createQuery(Integer.class, "idService", "Service identifier"))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL).implementation(String.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    private <T> org.springdoc.core.fn.builders.parameter.Builder createQuery(Class<T> clazz, String name, String description) {
        return parameterBuilder().in(QUERY).implementation(clazz).required(true).name(name).description(description);
    }

    private <T> org.springdoc.core.fn.builders.parameter.Builder createPath(Class<T> clazz, String name, String description) {
        return parameterBuilder().in(PATH).implementation(clazz).required(true).name(name).description(description);
    }
}
