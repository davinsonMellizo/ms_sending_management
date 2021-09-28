package co.com.bancolombia.api.services.alert;

import co.com.bancolombia.api.dto.AlertDTO;
import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.error.Error;
import org.springdoc.core.fn.builders.operation.Builder;

import java.util.function.Consumer;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;
import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;

public class AlertDocumentationApi {

    private final static String TAG = "Alert";
    private final static String ERROR = "Error";
    private final static String SUCCESSFUL = "successful";

    protected Consumer<Builder> save() {
        return ops -> ops.tag(TAG)
                .operationId("SaveAlert").summary("Save Alert")
                .description("Create new Alert").tags(new String[]{TAG})
                .requestBody(requestBodyBuilder().description("Alert to create").required(true).implementation(AlertDTO.class))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL).implementation(Alert.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    protected Consumer<Builder> find() {
        return ops -> ops.tag(TAG)
                .operationId("findAlert").summary("Find Alert")
                .description("Find Alerts by id").tags(new String[]{TAG})
                .parameter(createHeader(String.class, "id", "Alert identifier"))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    protected Consumer<Builder> update() {
        return ops -> ops.tag(TAG)
                .operationId("updateAlert").summary("Update Alert")
                .description("Update Alert by id").tags(new String[]{TAG})
                .requestBody(requestBodyBuilder().description("Alert to Update").required(true).implementation(AlertDTO.class))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL).implementation(Alert.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    protected Consumer<Builder> delete() {
        return ops -> ops.tag(TAG)
                .operationId("deleteAlert").summary("Delete Alert")
                .description("Delete a Alert by id").tags(new String[]{TAG})
                .parameter(createHeader(String.class, "id", "Alert identifier"))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL).implementation(String.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    private <T> org.springdoc.core.fn.builders.parameter.Builder createHeader(Class<T> clazz, String name, String description) {
        return parameterBuilder().in(PATH).implementation(clazz).required(true).name(name).description(description);
    }
}
