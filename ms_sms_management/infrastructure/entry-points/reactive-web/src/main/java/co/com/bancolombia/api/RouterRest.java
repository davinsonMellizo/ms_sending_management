package co.com.bancolombia.api;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;


@Configuration
@RequiredArgsConstructor
public class RouterRest extends SendSmsDocumentationApi {
    private final ApiProperties apiProperties;
    @Bean
    public RouterFunction<ServerResponse> routerFunctionSendAlert(Handler handler) {
        final String url = apiProperties.getSendSms();
        return route().POST(url, accept(APPLICATION_JSON), handler::sendSms, send()).build();
    }
}
