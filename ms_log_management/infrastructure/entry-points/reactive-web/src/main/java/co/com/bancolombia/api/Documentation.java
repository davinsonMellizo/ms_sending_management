package co.com.bancolombia.api;

import org.springdoc.core.fn.builders.operation.Builder;

import java.util.function.Consumer;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;
import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;

public class Documentation {

    private final static String TAG = "LOGS";
    private final static String ERROR = "Error";
    private final static String SUCCESSFUL = "successful";

    protected Consumer<Builder> find() {
        return ops -> ops.tag(TAG)
                .operationId("findLogs").summary("Find Logs")
                .description("Find Logs by parameter").tags(new String[]{TAG})
                .parameter(createHeader(String.class, "document-number", "Document Number"))
                .parameter(createHeader(String.class, "document-type", "Document Type"))
                .parameter(createHeader(String.class, "contact", "Contact Value"))
                .parameter(createHeader(String.class, "consumer", "Code Consumer"))
                .parameter(createHeader(String.class, "start-date", "Search start date "))
                .parameter(createHeader(String.class, "end-date", "Search end date "))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    private <T> org.springdoc.core.fn.builders.parameter.Builder createHeader(Class<T> clazz, String name, String description) {
        return parameterBuilder().in(PATH).implementation(clazz).name(name).description(description);
    }
}
