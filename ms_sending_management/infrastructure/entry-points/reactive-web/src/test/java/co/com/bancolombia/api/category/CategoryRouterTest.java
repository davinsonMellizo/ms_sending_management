package co.com.bancolombia.api.category;

import co.com.bancolombia.api.ApiProperties;
import co.com.bancolombia.api.BaseIntegration;
import co.com.bancolombia.api.commons.handlers.ExceptionHandler;
import co.com.bancolombia.api.commons.handlers.ValidatorHandler;
import co.com.bancolombia.api.services.category.CategoryHandler;
import co.com.bancolombia.api.services.category.CategoryRouter;
import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.model.category.Category;
import co.com.bancolombia.model.response.StatusResponse;
import co.com.bancolombia.usecase.category.CategoryUseCase;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static co.com.bancolombia.commons.enums.BusinessErrorMessage.CATEGORY_NOT_FOUND;
import static co.com.bancolombia.commons.enums.TechnicalExceptionEnum.INTERNAL_SERVER_ERROR;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebFluxTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        CategoryRouter.class,
        CategoryHandler.class,
        ApiProperties.class,
        ValidatorHandler.class,
        ExceptionHandler.class
})
public class CategoryRouterTest extends BaseIntegration {

    @MockBean
    private CategoryUseCase useCase;
    private String request;
    private final Category category = new Category();
    private String url;
    private final static String ID = "/{id}";

    @BeforeEach
    public void init() {
        url = properties.getCategory();
        request = loadFileConfig("CategoryRequest.json", String.class);
    }

    @Test
    public void save() {
        when(useCase.saveCategory(any()))
                .thenReturn(Mono.just(category));
        statusAssertionsWebClientPost(url, request)
                .isOk()
                .expectBody(JsonNode.class)
                .returnResult();
        verify(useCase).saveCategory(any());
    }

    @Test
    public void findAll() {
        when(useCase.findAllCategories())
                .thenReturn(Mono.just(List.of(category)));
        final WebTestClient.ResponseSpec spec = webTestClient.get().uri(url)
                .exchange();
        spec.expectStatus().isOk();
        verify(useCase).findAllCategories();
    }

    @Test
    public void update() {
        when(useCase.updateCategory(any())).thenReturn(Mono.just(StatusResponse.<Category>builder()
                .actual(category).before(category).build()));
        statusAssertionsWebClientPut(url,
                request)
                .isOk()
                .expectBody(JsonNode.class)
                .returnResult();
        verify(useCase).updateCategory(any());
    }

    @Test
    public void delete() {
        when(useCase.deleteCategoryById(any()))
                .thenReturn(Mono.just(1));
        WebTestClient.ResponseSpec spec = webTestClient.delete().uri(properties.getCategory() + ID, "0")
                .exchange();
        spec.expectStatus().isOk();
        verify(useCase).deleteCategoryById(any());
    }

    @Test
    public void saveCategoryWithException() {
        when(useCase.saveCategory(any())).thenReturn(Mono.error(new TechnicalException(INTERNAL_SERVER_ERROR)));
        statusAssertionsWebClientPost(url,
                request)
                .is5xxServerError();
        verify(useCase).saveCategory(any());
    }

    @Test
    public void updateCategoryWithException() {
        when(useCase.updateCategory(any())).thenReturn(Mono.error(new BusinessException(CATEGORY_NOT_FOUND)));
        statusAssertionsWebClientPut(url,
                request)
                .is5xxServerError();
    }
}