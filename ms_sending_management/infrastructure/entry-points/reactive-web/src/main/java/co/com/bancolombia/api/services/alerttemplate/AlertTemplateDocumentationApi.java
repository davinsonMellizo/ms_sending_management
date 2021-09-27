package co.com.bancolombia.api.services.alerttemplate;

import co.com.bancolombia.config.model.alerttemplate.AlertTemplate;
import co.com.bancolombia.config.model.error.Error;
import org.springdoc.core.fn.builders.operation.Builder;

import java.util.function.Consumer;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;
import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;

public class AlertTemplateDocumentationApi {

    private final static String TAG = "Template";
    private final static String ERROR = "Error";
    private final static String SUCCESSFUL = "successful";

    protected Consumer<Builder> saveAlertTemplateAPI() {
        return ops -> ops.tag(TAG)
                .operationId("Save alert template").summary("Save alert template")
                .description("Create new alert template").tags(new String[]{TAG})
                .requestBody(requestBodyBuilder().description("Alert template to create").required(true).implementation(AlertTemplate.class))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL).implementation(AlertTemplate.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    protected Consumer<Builder> findAlertTemplateAPI() {
        return ops -> ops.tag(TAG)
                .operationId("findAlertTemplate").summary("Find alert template")
                .description("Find Alert template by id").tags(new String[]{TAG})
                .parameter(createHeader(String.class, "id", "Alert template identifier"))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    protected Consumer<Builder> deleteAlertTemplateAPI() {
        return ops -> ops.tag(TAG)
                .operationId("deleteAlertTemplate").summary("Delete alert Template")
                .description("Delete a alert template by id").tags(new String[]{TAG})
                .parameter(createHeader(String.class, "id", "Alert template identifier"))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL).implementation(String.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    private <T> org.springdoc.core.fn.builders.parameter.Builder createHeader(Class<T> clazz, String name, String description) {
        return parameterBuilder().in(PATH).implementation(clazz).required(true).name(name).description(description);
    }
}
