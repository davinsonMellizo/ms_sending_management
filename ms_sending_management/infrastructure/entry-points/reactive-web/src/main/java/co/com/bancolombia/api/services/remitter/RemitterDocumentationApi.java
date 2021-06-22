package co.com.bancolombia.api.services.remitter;

import co.com.bancolombia.model.error.Error;
import co.com.bancolombia.model.remitter.Remitter;
import org.springdoc.core.fn.builders.operation.Builder;

import java.util.function.Consumer;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;
import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;

public class RemitterDocumentationApi {

    private final static String TAG = "Remitter";
    private final static String ERROR = "Error";
    private final static String SUCCESSFUL = "successful";

    protected Consumer<Builder> saveRemitterAPI() {
        return ops -> ops.tag(TAG)
                .operationId("SaveRemitter").summary("Save Remitter")
                .description("Create new Remitter").tags(new String[]{TAG})
                .requestBody(requestBodyBuilder().description("Remitter to create").required(true).implementation(Remitter.class))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL).implementation(Remitter.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    protected Consumer<Builder> findRemitterAPI() {
        return ops -> ops.tag(TAG)
                .operationId("findRemitter").summary("Find Remitter")
                .description("Find Remitter by id").tags(new String[]{TAG})
                .parameter(createHeader(String.class, "id", "Remitter identifier"))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    protected Consumer<Builder> findAllRemitterAPI() {
        return ops -> ops.tag(TAG)
                .operationId("findAllRemitter").summary("Find all Remitters")
                .description("Find all Remitters by id").tags(new String[]{TAG})
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    protected Consumer<Builder> updateRemitterAPI() {
        return ops -> ops.tag(TAG)
                .operationId("updateRemitter").summary("Update Remitter")
                .description("Update Remitter by id").tags(new String[]{TAG})
                .requestBody(requestBodyBuilder().description("Remitter to Update").required(true).implementation(Remitter.class))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL).implementation(Remitter.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    protected Consumer<Builder> deleteRemitterAPI() {
        return ops -> ops.tag(TAG)
                .operationId("deleteRemitter").summary("Delete Remitter")
                .description("Delete a Remitter by id").tags(new String[]{TAG})
                .parameter(createHeader(String.class, "id", "Remitter identifier"))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL).implementation(String.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    private <T> org.springdoc.core.fn.builders.parameter.Builder createHeader(Class<T> clazz, String name, String description) {
        return parameterBuilder().in(PATH).implementation(clazz).required(true).name(name).description(description);
    }
}
