package co.com.bancolombia.model.category.gateways;

import co.com.bancolombia.model.category.Category;
import co.com.bancolombia.model.response.StatusResponse;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CategoryGateway {

    Mono<List<Category>> findAll();

    Mono<Category> findCategoryById(Integer id);

    Mono<Category> saveCategory(Category category);

    Mono<StatusResponse<Category>> updateCategory(Category category);

    Mono<Integer> deleteCategoryById(Integer id);
}
