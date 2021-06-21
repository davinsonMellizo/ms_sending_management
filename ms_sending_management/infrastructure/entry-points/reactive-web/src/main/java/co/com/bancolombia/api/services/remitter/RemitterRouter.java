package co.com.bancolombia.api.services.remitter;

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
public class RemitterRouter extends RemitterDocumentationApi {
    private final ApiProperties apiProperties;
    private static String ID = "/{id}";

    @Bean
    public RouterFunction<ServerResponse> routerFunctionRemitter(RemitterHandler handler) {
        return route().POST(apiProperties.getRemitter(), accept(APPLICATION_JSON), handler::saveRemitter, saveRemitterAPI()).build()
                .and(route().PUT(apiProperties.getRemitter(), accept(APPLICATION_JSON), handler::updateRemitter, updateRemitterAPI()).build())
                .and(route().GET(apiProperties.getRemitter(), accept(APPLICATION_JSON), handler::findAllRemitter, findAllRemitterAPI()).build())
                .and(route().GET(apiProperties.getRemitter() + ID, accept(APPLICATION_JSON), handler::findRemitter, findRemitterAPI()).build())
                .and(route().DELETE(apiProperties.getRemitter() + ID, accept(APPLICATION_JSON), handler::deleteRemitter, deleteRemitterAPI()).build());
    }
}
