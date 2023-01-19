package co.com.bancolombia.api.services.clientmacd;

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
public class ClientMcdRouter extends ClientMcdDocumentationApi {
    private final ApiProperties apiProperties;
    private static final String MCD = "-macd";

    @Bean
    public RouterFunction<ServerResponse> routerFunctionClientMcd(ClientMcdHandler clientMcdHandler) {
        final String url = apiProperties.getClient();
        return route().PUT(url + MCD, accept(APPLICATION_JSON), clientMcdHandler::updateClientMcd,
                update()).build();
    }
}
