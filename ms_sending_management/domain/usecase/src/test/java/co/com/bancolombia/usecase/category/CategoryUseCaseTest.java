package co.com.bancolombia.usecase.category;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.category.Category;
import co.com.bancolombia.model.category.gateways.CategoryGateway;
import co.com.bancolombia.model.response.StatusResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryUseCaseTest {

    @InjectMocks
    private CategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    private final Category category = new Category();

    @BeforeEach
    private void init() {
        category.setId(1);
    }

    @Test
    public void findCategoryById() {
        when(categoryGateway.findCategoryById(anyInt()))
                .thenReturn(Mono.just(category));
        StepVerifier
                .create(useCase.findCategoryById(category.getId()))
                .expectNextCount(1)
                .verifyComplete();
        verify(categoryGateway).findCategoryById(anyInt());
    }

    @Test
    public void findAllCategories() {
        when(categoryGateway.findAll())
                .thenReturn(Mono.just(List.of(category)));
        StepVerifier
                .create(useCase.findAllCategories())
                .consumeNextWith(providers -> assertEquals(1, providers.size()))
                .verifyComplete();
        verify(categoryGateway).findAll();
    }

    @Test
    public void saveCategory() {
        when(categoryGateway.saveCategory(any()))
                .thenReturn(Mono.just(category));
        StepVerifier
                .create(useCase.saveCategory(category))
                .assertNext(response -> response
                        .getId().equals(category.getId()))
                .verifyComplete();
        verify(categoryGateway).saveCategory(any());
    }

    @Test
    public void updateCategory() {
        when(categoryGateway.updateCategory(any()))
                .thenReturn(Mono.just(StatusResponse.<Category>builder()
                        .actual(category).before(category).build()));
        StepVerifier
                .create(useCase.updateCategory(category))
                .assertNext(response -> response
                        .getActual().getId()
                        .equals(category.getId()))
                .verifyComplete();
        verify(categoryGateway).updateCategory(any());
    }

    @Test
    public void deleteCategory() {
        when(categoryGateway.findCategoryById(anyInt()))
                .thenReturn(Mono.just(category));
        when(categoryGateway.deleteCategoryById(any()))
                .thenReturn(Mono.just(category.getId()));
        StepVerifier.create(useCase.deleteCategoryById(category.getId()))
                .expectNextCount(1)
                .verifyComplete();
        verify(categoryGateway).deleteCategoryById(any());
    }

    @Test
    public void updateCategoryWithException() {
        when(categoryGateway.updateCategory(any()))
                .thenReturn(Mono.empty());
        useCase.updateCategory(category)
                .as(StepVerifier::create)
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    public void deleteCategoryWithException() {
        when(categoryGateway.findCategoryById(anyInt()))
                .thenReturn(Mono.empty());
        useCase.deleteCategoryById(category.getId())
                .as(StepVerifier::create)
                .expectError(BusinessException.class)
                .verify();
    }
}