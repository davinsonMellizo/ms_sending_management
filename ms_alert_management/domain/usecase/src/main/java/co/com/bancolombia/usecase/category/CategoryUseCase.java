package co.com.bancolombia.usecase.category;

import co.com.bancolombia.commons.exceptions.BusinessException;
import co.com.bancolombia.model.category.Category;
import co.com.bancolombia.model.category.gateways.CategoryGateway;
import co.com.bancolombia.model.response.StatusResponse;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

import static co.com.bancolombia.commons.enums.BusinessErrorMessage.CATEGORY_NOT_FOUND;

@RequiredArgsConstructor
public class CategoryUseCase {

    private final CategoryGateway categoryGateway;

    public Mono<List<Category>> findAllCategories() {
        return categoryGateway.findAll()
                .filter(categories -> !categories.isEmpty())
                .switchIfEmpty(Mono.error(new BusinessException(CATEGORY_NOT_FOUND)));
    }

    public Mono<Category> findCategoryById(Integer id) {
        return categoryGateway.findCategoryById(id)
                .switchIfEmpty(Mono.error(new BusinessException(CATEGORY_NOT_FOUND)));
    }

    public Mono<Category> saveCategory(Category category) {
        return categoryGateway.saveCategory(category);
    }

    public Mono<StatusResponse<Category>> updateCategory(Category category) {
        return categoryGateway.updateCategory(category)
                .switchIfEmpty(Mono.error(new BusinessException(CATEGORY_NOT_FOUND)));
    }

    public Mono<Integer> deleteCategoryById(Integer id) {
        return categoryGateway.findCategoryById(id)
                .switchIfEmpty(Mono.error(new BusinessException(CATEGORY_NOT_FOUND)))
                .map(Category::getId)
                .flatMap(categoryGateway::deleteCategoryById);
    }
}
