package co.com.bancolombia.api.services.event;

import org.springdoc.core.fn.builders.operation.Builder;

import java.util.function.Consumer;

public class EventDocumentationApi {

    private final static String TAG = "Event";

    protected Consumer<Builder> eventRegister() {
        return ops -> ops.tag(TAG)
                .operationId("EventRegister").summary("Event Register")
                .description("Event Register").tags(new String[]{TAG});
    }

}
