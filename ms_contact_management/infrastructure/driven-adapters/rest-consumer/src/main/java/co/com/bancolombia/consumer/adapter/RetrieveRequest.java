package co.com.bancolombia.consumer.adapter;

import co.com.bancolombia.model.Request;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
public class RetrieveRequest extends Request {
    private DataRequest data;

}

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
class DataRequest implements Serializable {
    private CustomerIdentification customerIdentification;
}

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
class CustomerIdentification implements Serializable{
    private String documentType;
    private String documentNumber;
}
