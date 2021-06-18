package co.com.bancolombia.api.services.provider;

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
public class ProviderRouter extends ProviderDocumentationApi {
    private final ApiProperties apiProperties;
    private static String ID = "/{id}";

    @Bean
    public RouterFunction<ServerResponse> routerFunctionProvider(ProviderHandler handler) {
        return route().POST(apiProperties.getProvider(), accept(APPLICATION_JSON), handler::saveProvider, saveProviderAPI()).build()
                .and(route().PUT(apiProperties.getProvider(), accept(APPLICATION_JSON), handler::updateProvider, updateProviderAPI()).build())
                .and(route().GET(apiProperties.getProvider()+ ID, accept(APPLICATION_JSON), handler::findProviderById, findProviderAPI()).build())
                .and(route().DELETE(apiProperties.getProvider()+ ID, accept(APPLICATION_JSON), handler::deleteProviderById, deleteProviderAPI()).build());
    }
}
