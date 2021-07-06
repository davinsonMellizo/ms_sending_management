package co.com.bancolombia.alerttemplate.data;

import co.com.bancolombia.model.alerttemplate.AlertTemplate;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AlertTemplateMapper {

    AlertTemplate toEntity(AlertTemplateData alertTemplateData);

}
