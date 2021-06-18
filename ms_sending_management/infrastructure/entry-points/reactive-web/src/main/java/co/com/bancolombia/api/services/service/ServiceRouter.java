package co.com.bancolombia.api.services.service;

import co.com.bancolombia.api.ApiProperties;
import co.com.bancolombia.api.services.remitter.RemitterDocumentationApi;
import co.com.bancolombia.api.services.remitter.RemitterHandler;
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
public class ServiceRouter extends ServiceDocumentationApi {
    private final ApiProperties apiProperties;
    private static String ID = "/{id}";

    @Bean
    public RouterFunction<ServerResponse> routerFunctionService(ServiceHandler handler) {
        return route().POST(apiProperties.getService(), accept(APPLICATION_JSON), handler::saveService, saveServiceAPI()).build()
                .and(route().PUT(apiProperties.getService(), accept(APPLICATION_JSON), handler::updateService, updateServiceAPI()).build())
                .and(route().GET(apiProperties.getService()+ID, accept(APPLICATION_JSON), handler::findService, findServiceAPI()).build())
                .and(route().DELETE(apiProperties.getService()+ID, accept(APPLICATION_JSON), handler::deleteService, deleteServiceAPI()).build());
    }
}
