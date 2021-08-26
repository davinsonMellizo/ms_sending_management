package co.com.bancolombia.alerttemplate.data;

import co.com.bancolombia.model.alerttemplate.AlertTemplate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AlertTemplateMapper {

    AlertTemplate toEntity(AlertTemplateData alertTemplateData);

    @Mapping(target = "isNew", ignore = true)
    AlertTemplateData toData(AlertTemplate alertTemplate);

}
