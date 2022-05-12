package co.com.bancolombia.category;

import co.com.bancolombia.model.category.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CategoryRepositoryImplTest {

    @Autowired
    private CategoryRepositoryImplement repositoryImpl;
    private final Category category = new Category();

    @BeforeEach
    public void init() {
        category.setId(1);
        category.setName("cat1");
    }

    @Test
    void findIdCategory() {
        StepVerifier.create(repositoryImpl.findCategoryById(category.getId()))
                .consumeNextWith(providerFound -> assertEquals(category.getId(), providerFound.getId()))
                .verifyComplete();
    }

}