package co.com.bancolombia.alerttransaction.data;

import co.com.bancolombia.model.alerttransaction.AlertTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel = "spring")
public interface AlertTransactionMapper {
    AlertTransaction toEntity(AlertTransactionData alertTransactionData);

}
