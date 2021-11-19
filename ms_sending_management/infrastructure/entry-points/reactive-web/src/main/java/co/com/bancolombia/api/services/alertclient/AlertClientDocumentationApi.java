package co.com.bancolombia.api.services.alertclient;

import co.com.bancolombia.api.dto.AlertClientDTO;
import co.com.bancolombia.model.alertclient.AlertClient;
import co.com.bancolombia.model.client.ResponseClient;
import co.com.bancolombia.model.error.Error;
import org.springdoc.core.fn.builders.operation.Builder;

import java.util.function.Consumer;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;
import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;
import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;

public class AlertClientDocumentationApi {

    private final static String TAG = "Alert Client";
    private final static String ERROR = "Error";
    private final static String SUCCESSFUL = "successful";

    protected Consumer<Builder> save() {
        return ops -> ops.tag(TAG)
                .operationId("associateAlertToClient").summary("Associate alert to client ")
                .description("Associate alert to client").tags(new String[]{TAG})
                .requestBody(requestBodyBuilder().description("Relation alert-client to create").required(true).implementation(AlertClientDTO.class))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL).implementation(AlertClient.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }
    protected Consumer<Builder> basicKit() {
        return ops -> ops.tag(TAG)
                .operationId("matchClientWithBasicKit").summary("Associate alerts to client")
                .description("Associate alerts to client  by Basic Kit").tags(new String[]{TAG})
                .parameter(createPath(Long.class, "document-number", "Client document"))
                .parameter(createPath(Integer.class, "document-type", "Client document type"))
                .parameter(createPath(String.class, "association-origin", "Association Origin"))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL).implementation(ResponseClient.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    protected Consumer<Builder> update() {
        return ops -> ops.tag(TAG)
                .operationId("updateAlertClient").summary("Update alert client")
                .description("Update number-operations and amountenable by client").tags(new String[]{TAG})
                .requestBody(requestBodyBuilder().description("Alert client to Update").required(true).implementation(AlertClientDTO[].class))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL).implementation(AlertClient.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    protected Consumer<Builder> find() {
        return ops -> ops.tag(TAG)
                .operationId("findAlertClient").summary("find all alert client relations")
                .description("Find all alert relations with client by document-number and document-type").tags(new String[]{TAG})
                .parameter(createPath(String.class, "document-number", "Client document"))
                .parameter(createPath(String.class, "document-type", "Client document type"))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL).implementationArray(AlertClientDTO.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    protected Consumer<Builder> delete() {
        return ops -> ops.tag(TAG)
                .operationId("deleteAlertTransaction").summary("Delete alert association")
                .description("Delete alert association with transaction and consumer").tags(new String[]{TAG})
                .parameter(createQuery(String.class, "idAlert", "id Alert identifier"))
                .parameter(createPath(String.class, "document-number", "Client document"))
                .parameter(createPath(String.class, "document-type", "Client document type"))
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
