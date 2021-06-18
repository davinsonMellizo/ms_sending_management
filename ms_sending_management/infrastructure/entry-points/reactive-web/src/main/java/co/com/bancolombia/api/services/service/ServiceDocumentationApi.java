package co.com.bancolombia.api.services.service;

import co.com.bancolombia.model.error.Error;
import co.com.bancolombia.model.service.Service;
import org.springdoc.core.fn.builders.operation.Builder;

import java.util.function.Consumer;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;
import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;

public class ServiceDocumentationApi {

    private final static String tag = "Service";
    private final static String error = "Error";
    private final static String successful = "successful";

    protected Consumer<Builder> saveServiceAPI() {
        return ops -> ops.tag(tag)
                .operationId("Save service").summary("Save service")
                .description("Create new service").tags(new String[]{tag})
                .requestBody(requestBodyBuilder().description("Service to create").required(true).implementation(Service.class))
                .response(responseBuilder().responseCode("200").description(successful).implementation(Service.class))
                .response(responseBuilder().responseCode("500").description(error).implementation(Error.class));
    }

    protected Consumer<Builder> findServiceAPI() {
        return ops -> ops.tag(tag)
                .operationId("findService").summary("Find Service")
                .description("Find Service by id").tags(new String[]{tag})
                .parameter(createHeader(String.class, "id", "Service identifier"))
                .response(responseBuilder().responseCode("500").description(error).implementation(Error.class));
    }

    protected Consumer<Builder> updateServiceAPI() {
        return ops -> ops.tag(tag)
                .operationId("updateService").summary("Update Service")
                .description("Update Service by id").tags(new String[]{tag})
                .requestBody(requestBodyBuilder().description("Service to Update").required(true).implementation(Service.class))
                .response(responseBuilder().responseCode("200").description(successful).implementation(Service.class))
                .response(responseBuilder().responseCode("500").description(error).implementation(Error.class));
    }

    protected Consumer<Builder> deleteServiceAPI() {
        return ops -> ops.tag(tag)
                .operationId("deleteService").summary("Delete Service")
                .description("Delete a Service by id").tags(new String[]{tag})
                .parameter(createHeader(String.class, "id", "Service identifier"))
                .response(responseBuilder().responseCode("200").description(successful).implementation(String.class))
                .response(responseBuilder().responseCode("500").description(error).implementation(Error.class));
    }

    private <T> org.springdoc.core.fn.builders.parameter.Builder createHeader(Class<T> clazz, String name, String description) {
        return parameterBuilder().in(PATH).implementation(clazz).required(true).name(name).description(description);
    }
}
