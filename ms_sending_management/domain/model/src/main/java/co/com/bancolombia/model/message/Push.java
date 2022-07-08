package co.com.bancolombia.model.message;

import co.com.bancolombia.Request;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Push extends Request {
    private String applicationCode;
    private String categoryId;
    private String consumerId;
    private String customerDocumentNumber;
    private String customerDocumentType;
    private String message;
    private String customerMdmKey;
    private String customerNickname;
}
