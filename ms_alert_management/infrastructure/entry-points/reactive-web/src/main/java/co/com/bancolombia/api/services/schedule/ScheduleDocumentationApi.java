package co.com.bancolombia.api.services.schedule;

import co.com.bancolombia.model.error.Error;
import co.com.bancolombia.model.schedule.Schedule;
import org.springdoc.core.fn.builders.operation.Builder;

import java.util.function.Consumer;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.PATH;
import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;

public class ScheduleDocumentationApi {

    private static final String TAG = "Schedule";
    private static final String ERROR = "Error";
    private static final String SUCCESSFUL = "successful";

    protected Consumer<Builder> save() {
        return ops -> ops.tag(TAG)
                .operationId("SaveSchedule").summary("Save Schedule")
                .description("Create new schedule").tags(new String[]{TAG})
                .requestBody(requestBodyBuilder().description("Schedule to create").required(true)
                        .implementation(Schedule.class))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL)
                        .implementation(Schedule.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    protected Consumer<Builder> find() {
        return ops -> ops.tag(TAG)
                .operationId("findSchedule").summary("Find Schedule")
                .description("Find schedule by id").tags(new String[]{TAG})
                .parameter(createHeader(String.class, "id", "Schedule identifier"))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL)
                        .implementation(Schedule.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    protected Consumer<Builder> update() {
        return ops -> ops.tag(TAG)
                .operationId("updateSchedule").summary("Update schedule")
                .description("Update Schedule by id").tags(new String[]{TAG})
                .requestBody(requestBodyBuilder().description("Schedule to Update").required(true)
                        .implementation(Schedule.class))
                .response(responseBuilder().responseCode("200").description(SUCCESSFUL)
                        .implementation(Schedule.class))
                .response(responseBuilder().responseCode("500").description(ERROR).implementation(Error.class));
    }

    private <T> org.springdoc.core.fn.builders.parameter.Builder createHeader(Class<T> clazz, String name, String description) {
        return parameterBuilder().in(PATH).implementation(clazz).required(true).name(name).description(description);
    }

}
