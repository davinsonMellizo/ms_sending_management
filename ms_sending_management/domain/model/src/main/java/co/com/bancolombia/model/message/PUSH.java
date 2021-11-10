package co.com.bancolombia.model.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PUSH {
    private Data data;

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(toBuilder = true)
    public static class Data{
        private SendMessage sendMessage;

    }

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(toBuilder = true)
    public static class SendMessage{
        private CustomerIdentification customerIdentification;
        private String customerNickname;
        private String customerMdmKey;
        private String message;
        private String categoryId;
        private String consumerId;
        private String applicationCode;
    }

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(toBuilder = true)
    public static class CustomerIdentification{
        private String customerDocumentType;
        private String customerDocumentNumber;
    }
}
