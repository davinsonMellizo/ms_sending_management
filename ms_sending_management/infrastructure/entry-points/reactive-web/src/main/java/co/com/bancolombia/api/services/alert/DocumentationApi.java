package co.com.bancolombia.api.services.alert;

import co.com.bancolombia.api.dto.AlertDTO;
import co.com.bancolombia.model.alert.Alert;
import co.com.bancolombia.model.error.Error;
import org.springdoc.core.fn.builders.operation.Builder;

import java.util.function.Consumer;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;
import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;

public class DocumentationApi {

    private final String tag = "Alerts";
    private final String error = "Error";
    private final String successful = "successful";

    protected Consumer<Builder> saveAlertAPI() {
        return ops -> ops.tag(tag)
                .operationId("SaveAlert").summary("Save Alert")
                .description("Create new Alert").tags(new String[]{tag})
                .requestBody(requestBodyBuilder().description("Alert to create").required(true).implementation(AlertDTO.class))
                .response(responseBuilder().responseCode("200").description(successful).implementation(Alert.class))
                .response(responseBuilder().responseCode("400").description("Bad Request").implementation(Error.class))
                .response(responseBuilder().responseCode("500").description(error).implementation(Error.class));
    }

    protected Consumer<Builder> findAlertAPI() {
        return ops -> ops.tag(tag)
                .operationId("findAlerts").summary("Find Alerts")
                .description("Find Alerts by id").tags(new String[]{tag})
                .parameter(createHeader(String.class, "id", "Alert identifier"))
                .response(responseBuilder().responseCode("200").description(successful).implementation(Alert.class))
                .response(responseBuilder().responseCode("500").description(error).implementation(Error.class));
    }

    protected Consumer<Builder> updateAlertAPI() {
        return ops -> ops.tag(tag)
                .operationId("updateAlert").summary("Update Alert")
                .description("Update Alert by id").tags(new String[]{tag})
                .requestBody(requestBodyBuilder().description("Alert to Update").required(true).implementation(AlertDTO.class))
                .response(responseBuilder().responseCode("200").description(successful).implementation(Alert.class))
                .response(responseBuilder().responseCode("400").description("Bad Request").implementation(Error.class))
                .response(responseBuilder().responseCode("500").description(error).implementation(Error.class));
    }

    protected Consumer<Builder> deleteAlertAPI() {
        return ops -> ops.tag(tag)
                .operationId("deleteAlert").summary("Delete Alert")
                .description("Delete a Alert by id").tags(new String[]{tag})
                .parameter(createHeader(String.class, "id", "Alert identifier"))
                .response(responseBuilder().responseCode("200").description(successful).implementation(String.class))
                .response(responseBuilder().responseCode("500").description(error).implementation(Error.class));
    }

    private <T> org.springdoc.core.fn.builders.parameter.Builder createHeader(Class<T> clazz, String name, String description) {
        return parameterBuilder().in(QUERY).implementation(clazz).required(true).name(name).description(description);
    }
}
