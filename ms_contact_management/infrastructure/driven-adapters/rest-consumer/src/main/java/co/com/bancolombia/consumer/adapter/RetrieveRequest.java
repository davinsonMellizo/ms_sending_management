package co.com.bancolombia.consumer.adapter;

import co.com.bancolombia.model.Request;
import lombok.*;
import lombok.experimental.SuperBuilder;

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
class DataRequest {
    private CustomerIdentification customerIdentification;
}

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
class CustomerIdentification {
    private String documentType;
    private String documentNumber;
}
