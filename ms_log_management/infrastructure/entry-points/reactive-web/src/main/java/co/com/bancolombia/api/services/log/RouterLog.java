package co.com.bancolombia.api.services.log;

import co.com.bancolombia.api.ApiProperties;
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
public class RouterLog extends LogDocumentationApi{
    private final ApiProperties apiProperties;

    @Bean
    public RouterFunction<ServerResponse> routerFunctionLog(HandlerLog handlerLog) {
        final String url = apiProperties.getLog();
        return route().GET(url, accept(APPLICATION_JSON), handlerLog::findLogsByDate, find()).build();
    }
}
