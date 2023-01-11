package co.com.bancolombia.category.data;

import co.com.bancolombia.model.category.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toEntity(CategoryData categoryData);

}
