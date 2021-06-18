package co.com.bancolombia.api.services.provider;

import co.com.bancolombia.api.dto.ProviderDTO;
import co.com.bancolombia.model.error.Error;
import co.com.bancolombia.model.provider.Provider;
import org.springdoc.core.fn.builders.operation.Builder;

import java.util.function.Consumer;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;
import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;

public class ProviderDocumentationApi {

    private final static String tag = "Provider";
    private final static String error = "Error";
    private final static String successful = "successful";

    protected Consumer<Builder> saveProviderAPI() {
        return ops -> ops.tag(tag)
                .operationId("SaveProvider").summary("Save Provider")
                .description("Create new Provider").tags(new String[]{tag})
                .requestBody(requestBodyBuilder().description("Provider to create").required(true).implementation(ProviderDTO.class))
                .response(responseBuilder().responseCode("200").description(successful).implementation(Provider.class))
                .response(responseBuilder().responseCode("500").description(error).implementation(Error.class));
    }

    protected Consumer<Builder> findProviderAPI() {
        return ops -> ops.tag(tag)
                .operationId("findProvider").summary("Find Provider")
                .description("Find Provider by id").tags(new String[]{tag})
                .parameter(createHeader(String.class, "id", "Provider identifier"))
                .response(responseBuilder().responseCode("500").description(error).implementation(Error.class));
    }

    protected Consumer<Builder> updateProviderAPI() {
        return ops -> ops.tag(tag)
                .operationId("updateProvider").summary("Update Provider")
                .description("Update Provider by code").tags(new String[]{tag})
                .requestBody(requestBodyBuilder().description("Provider to Update").required(true).implementation(ProviderDTO.class))
                .response(responseBuilder().responseCode("200").description(successful).implementation(Provider.class))
                .response(responseBuilder().responseCode("500").description(error).implementation(Error.class));
    }

    protected Consumer<Builder> deleteProviderAPI() {
        return ops -> ops.tag(tag)
                .operationId("deleteProvider").summary("Delete Provider")
                .description("Delete a Provider by id").tags(new String[]{tag})
                .parameter(createHeader(String.class, "id", "Provider identifier"))
                .response(responseBuilder().responseCode("200").description(successful).implementation(String.class))
                .response(responseBuilder().responseCode("500").description(error).implementation(Error.class));
    }

    private <T> org.springdoc.core.fn.builders.parameter.Builder createHeader(Class<T> clazz, String name, String description) {
        return parameterBuilder().in(PATH).implementation(clazz).required(true).name(name).description(description);
    }
}
