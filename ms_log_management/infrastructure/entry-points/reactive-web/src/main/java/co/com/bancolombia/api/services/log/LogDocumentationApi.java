package co.com.bancolombia.api.services.log;


import co.com.bancolombia.model.error.Error;
import org.springdoc.core.fn.builders.operation.Builder;

import java.util.function.Consumer;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;
import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;

public class LogDocumentationApi {

    private final String TAG = "Log";
    private final String ERROR = "Error";
    private final String SUCCESSFUL = "successful";


    protected Consumer<Builder> find() {
        return ops -> ops.tag(TAG)
                .operationId("findLogs").summary("Find logs")
                .description("Find Logs by date").tags(new String[]{TAG})
                .parameter(createHeader(String.class, "Start Data", "Initial search date"))
                .parameter(createHeader(String.class, "End Date", "Search end date"))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL)
                        .implementation(String.class))
                .response(responseBuilder().responseCode("400").description("Bad Request")
                        .implementation(String.class))
                .response(responseBuilder().responseCode("500").description(ERROR)
                        .implementation(Error.class));
    }

    private <T> org.springdoc.core.fn.builders.parameter.Builder createHeader(Class<T> clazz,
                                                                              String name, String description) {
        return parameterBuilder().in(HEADER).implementation(clazz).required(true)
                .name(name).description(description);
    }
}
