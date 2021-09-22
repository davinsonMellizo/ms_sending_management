package co.com.bancolombia.category;

import co.com.bancolombia.category.data.CategoryData;
import co.com.bancolombia.category.data.CategoryMapper;
import co.com.bancolombia.commons.exceptions.TechnicalException;
import co.com.bancolombia.drivenadapters.TimeFactory;
import co.com.bancolombia.model.category.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class CategoryRepositoryImplExceptionTest {

    @InjectMocks
    private CategoryRepositoryImplement repositoryImpl;
    @Mock
    private CategoryRepository repository;
    @Spy
    private CategoryMapper mapper = Mappers.getMapper(CategoryMapper.class);
    private static final LocalDateTime NOW = LocalDateTime.now();
    @Mock
    private TimeFactory timeFactory;

    private final Category category = new Category();
    private final CategoryData categoryData = new CategoryData();

    @BeforeEach
    public void init() {
        category.setId(1);
        category.setName("cat1");
    }

    @Test
    public void findCategoryByIdWithException() {
        when(repository.findById(anyInt()))
                .thenReturn(Mono.error(RuntimeException::new));
        repositoryImpl.findCategoryById(category.getId())
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }

    @Test
    public void findAllCategoryWithException() {
        when(repository.findAll())
                .thenReturn(Flux.error(RuntimeException::new));
        repositoryImpl.findAll()
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }

    @Test
    public void saveCategoryWithException() {
        when(timeFactory.now()).thenReturn(NOW);
        when(repository.save(any()))
                .thenReturn(Mono.error(RuntimeException::new));
        repositoryImpl.saveCategory(category)
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }

    @Test
    public void updateCategoryWithException() {
        when(repository.save(any()))
                .thenReturn(Mono.error(RuntimeException::new));
        when(repository.findById(anyInt()))
                .thenReturn(Mono.just(categoryData));
        repositoryImpl.updateCategory(category)
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }

    @Test
    public void deleteCategoryWithException() {
        when(repository.deleteById(anyInt()))
                .thenReturn(Mono.error(RuntimeException::new));
        repositoryImpl.deleteCategoryById(category.getId())
                .as(StepVerifier::create)
                .expectError(TechnicalException.class)
                .verify();
    }
}