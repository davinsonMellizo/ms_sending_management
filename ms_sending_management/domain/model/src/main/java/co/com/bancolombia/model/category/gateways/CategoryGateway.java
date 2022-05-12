package co.com.bancolombia.model.category.gateways;

import co.com.bancolombia.model.category.Category;
import reactor.core.publisher.Mono;

public interface CategoryGateway {

    Mono<Category> findCategoryById(Integer id);

}
