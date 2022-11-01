package co.com.bancolombia.api.services.consumer;

import co.com.bancolombia.model.error.Error;
import org.springdoc.core.fn.builders.operation.Builder;

import java.util.function.Consumer;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;
import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;

public class ConsumerDocumentationApi {

    private static final String TAG = "Consumer";
    private static final String ERROR = "Error";
    private static final String SUCCESSFUL = "successful";

    protected Consumer<Builder> save() {
        return ops -> ops.tag(TAG)
                .operationId("SaveConsumer").summary("Save Consumer")
                .description("Create new Consumer").tags(new String[]{TAG})
                .requestBody(requestBodyBuilder().description("Consumer to create").required(true)
                        .implementation(co.com.bancolombia.model.consumer.Consumer.class))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL)
                        .implementation(co.com.bancolombia.model.consumer.Consumer.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    protected Consumer<Builder> find() {
        return ops -> ops.tag(TAG)
                .operationId("findConsumer").summary("Find Consumer")
                .description("Find Consumer by id").tags(new String[]{TAG})
                .parameter(createHeader(String.class, "id", "Consumer identifier"))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL)
                        .implementation(co.com.bancolombia.model.consumer.Consumer.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    protected Consumer<Builder> findAll() {
        return ops -> ops.tag(TAG)
                .operationId("findAllConsumer").summary("Find all Consumers")
                .description("Find all Consumers by id").tags(new String[]{TAG})
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL)
                        .implementationArray(co.com.bancolombia.model.consumer.Consumer.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    protected Consumer<Builder> update() {
        return ops -> ops.tag(TAG)
                .operationId("updateConsumer").summary("Update Consumer")
                .description("Update Consumer by id").tags(new String[]{TAG})
                .requestBody(requestBodyBuilder().description("Consumer to Update").required(true)
                        .implementation(co.com.bancolombia.model.consumer.Consumer.class))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL)
                        .implementation(co.com.bancolombia.model.consumer.Consumer.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    protected Consumer<Builder> delete() {
        return ops -> ops.tag(TAG)
                .operationId("deleteConsumer").summary("Delete consumer")
                .description("Delete a Consumer by id").tags(new String[]{TAG})
                .parameter(createHeader(String.class, "id", "Consumer identifier"))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL).implementation(String.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    private <T> org.springdoc.core.fn.builders.parameter.Builder createHeader(Class<T> clazz, String name, String description) {
        return parameterBuilder().in(PATH).implementation(clazz).required(true).name(name).description(description);
    }
}
