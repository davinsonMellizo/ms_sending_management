package co.com.bancolombia.api.services.providerservice;

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
public class ProviderServiceRouter extends ProviderServiceDocumentationApi {
    private final ApiProperties apiProperties;
    private final static String RELATION = "-service";
    private static String ID = "/{id}";

    @Bean
    public RouterFunction<ServerResponse> routerFunctionProviderService(ProviderServiceHandler handler) {
        final String url = apiProperties.getProvider() + RELATION;
        return route().POST(url, accept(APPLICATION_JSON), handler::saveProviderService, save()).build()
                .and(route().GET(url, accept(APPLICATION_JSON), handler::findAllProviderService, find()).build())
                .and(route().DELETE(url, accept(APPLICATION_JSON), handler::deleteProviderService, delete()).build());
    }
}
