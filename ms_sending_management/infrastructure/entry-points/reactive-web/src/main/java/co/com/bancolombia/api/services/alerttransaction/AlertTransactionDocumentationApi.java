package co.com.bancolombia.api.services.alerttransaction;

import co.com.bancolombia.api.dto.AlertTransactionDTO;
import co.com.bancolombia.config.model.alerttransaction.AlertTransaction;
import co.com.bancolombia.config.model.error.Error;
import org.springdoc.core.fn.builders.operation.Builder;

import java.util.function.Consumer;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;
import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;
import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;

public class AlertTransactionDocumentationApi {

    private final static String TAG = "Alert";
    private final static String ERROR = "Error";
    private final static String SUCCESSFUL = "successful";

    protected Consumer<Builder> save() {
        return ops -> ops.tag(TAG)
                .operationId("associateAlertToTransaction").summary("Associate alert to transaction ")
                .description("Associate alert to transaction and its consumer").tags(new String[]{TAG})
                .requestBody(requestBodyBuilder().description("Relation alert to create").required(true).implementation(AlertTransactionDTO.class))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL).implementation(AlertTransaction.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    protected Consumer<Builder> find() {
        return ops -> ops.tag(TAG)
                .operationId("findAlertTransaction").summary("find all alert relations")
                .description("Find all alert relations with transaction and consumer by alert").tags(new String[]{TAG})
                .parameter(createPath(String.class, "id", "Alert identifier"))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL).implementationArray(AlertTransaction.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    protected Consumer<Builder> delete() {
        return ops -> ops.tag(TAG)
                .operationId("deleteAlertTransaction").summary("Delete alert association")
                .description("Delete alert association with transaction and consumer").tags(new String[]{TAG})
                .parameter(createQuery(String.class, "idAlert", "Alert identifier"))
                .parameter(createQuery(String.class, "idConsumer", "Consumer identifier"))
                .parameter(createQuery(String.class, "idTransaction", "Transaction identifier"))
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
