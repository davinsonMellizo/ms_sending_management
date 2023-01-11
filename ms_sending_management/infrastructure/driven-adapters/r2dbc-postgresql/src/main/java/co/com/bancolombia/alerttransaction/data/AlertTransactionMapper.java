package co.com.bancolombia.alerttransaction.data;

import co.com.bancolombia.model.alerttransaction.AlertTransaction;
import org.mapstruct.Mapper;
@Mapper(componentModel = "spring")
public interface AlertTransactionMapper {
    AlertTransaction toEntity(AlertTransactionData alertTransactionData);

}
