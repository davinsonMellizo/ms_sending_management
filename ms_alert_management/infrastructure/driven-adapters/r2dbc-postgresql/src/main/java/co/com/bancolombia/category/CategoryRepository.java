package co.com.bancolombia.category;

import co.com.bancolombia.category.data.CategoryData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CategoryRepository extends ReactiveCrudRepository<CategoryData, Integer> {
}
