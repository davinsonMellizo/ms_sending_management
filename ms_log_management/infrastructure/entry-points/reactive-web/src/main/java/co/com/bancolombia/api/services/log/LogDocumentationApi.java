package co.com.bancolombia.api.services.log;


import co.com.bancolombia.model.error.Error;
import co.com.bancolombia.model.log.Log;
import org.springdoc.core.fn.builders.operation.Builder;

import java.util.function.Consumer;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;
import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;

public class LogDocumentationApi {

    private final String TAG = "Log";
    private final String ERROR = "Error";
    private final String SUCCESSFUL = "successful";
    private final String EXAMPLE = "2022-11-21T10:11:25.584569 Format: yyyy-MM-ddTHH:mm:ss.SSSSSS";


    protected Consumer<Builder> find() {

        return ops -> ops.tag(TAG)
                .operationId("findLogs")
                .summary("Find logs")
                .description("Search for records less than a month old").tags(new String[]{TAG})
                .parameter(createHeader(String.class, "", "document-number", "Document number"))
                .parameter(createHeader(String.class, "", "document-type", "Document Type"))
                .parameter(createHeader(String.class, "", "contact", "contact value"))
                .parameter(createHeader(String.class, "", "consumer", "Consumer code"))
                .parameter(createHeader(String.class, "", "provider", "Provider code"))
                .parameter(createHeader(String.class, EXAMPLE, "start-date", "Initial search date"))
                .parameter(createHeader(String.class, EXAMPLE, "end-date", "Search end date"))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL)
                        .implementationArray(Log.class))
                .response(responseBuilder().responseCode("500").description(ERROR)
                        .implementation(Error.class));
    }

    private <T> org.springdoc.core.fn.builders.parameter.Builder createHeader(Class<T> clazz, String example,
                                                                              String name, String description) {
        return parameterBuilder().in(HEADER).implementation(clazz).required(false)
                .name(name).description(description).example(example);
    }
}
