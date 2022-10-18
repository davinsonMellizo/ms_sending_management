package co.com.bancolombia.api.services.campaign;

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
public class CampaignRouter extends CampaignDocumentationApi {

    private static final String ALL = "/all";
    private final ApiProperties apiProperties;

    @Bean
    public RouterFunction<ServerResponse> routerFunctionCampaign(CampaignHandler handler) {
        final String url = apiProperties.getCampaign();
        return route().POST(url, accept(APPLICATION_JSON), handler::saveCampaign, save()).build()
                .and(route().PUT(url, accept(APPLICATION_JSON), handler::updateCampaign, update()).build())
                .and(route().GET(url + ALL, accept(APPLICATION_JSON), handler::findAllCampaign, findAll()).build())
                .and(route().GET(url, accept(APPLICATION_JSON), handler::findCampaign, find()).build())
                .and(route().DELETE(url, accept(APPLICATION_JSON), handler::deleteCampaign, delete()).build());
    }
}
