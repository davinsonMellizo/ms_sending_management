package co.com.bancolombia.api.services.event;

import co.com.bancolombia.api.ApiProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;


@Configuration
@RequiredArgsConstructor
public class EventRouter extends EventDocumentationApi {
    private final ApiProperties apiProperties;

    @Bean
    public RouterFunction<ServerResponse> routerFunctionAlert(EventHandler handler) {
        final String url = apiProperties.getEventRegister();
        return route().POST(url, accept(APPLICATION_JSON), handler::eventRegister, eventRegister()).build();
    }
}
