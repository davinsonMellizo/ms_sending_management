package co.com.bancolombia.api.services.alerttemplate;

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
public class AlertTemplateRouter extends AlertTemplateDocumentationApi {

    private final ApiProperties apiProperties;
    private final static String RELATION = "-template";
    private static String ID = "/{id}";

    @Bean
    public RouterFunction<ServerResponse> routerFunctionAlertTemplate(AlertTemplateHandler handler) {
        final String url = apiProperties.getAlert() + RELATION;
        return route().POST(url, accept(APPLICATION_JSON), handler::saveAlertTemplate,
                saveAlertTemplateAPI()).build()
                .and(route().GET(url + ID, accept(APPLICATION_JSON), handler::findAlertTemplate,
                        findAlertTemplateAPI()).build())
                .and(route().DELETE(url + ID, accept(APPLICATION_JSON), handler::deleteAlertTemplate,
                        deleteAlertTemplateAPI()).build());
    }
}
