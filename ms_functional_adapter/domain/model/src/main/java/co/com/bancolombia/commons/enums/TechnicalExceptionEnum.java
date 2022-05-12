package co.com.bancolombia.commons.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TechnicalExceptionEnum {

    TECHNICAL_EVENT_EXCEPTION("DMT0004", "An error has occurred sending event", true),
    TECHNICAL_CONFIG_TRANSACTION_NOT_FOUND("DAT0015", "Transaction not found in configuration", false),
    TECHNICAL_JMS_ERROR("DAT0014", "An error while processing the JMS", false),
    TECHNICAL_S3_EXCEPTION("DMT0006", "An error occurred while trying to get S3 object", false),
    TECHNICAL_SSL_CONTEXT_ERROR("DAT0016", "Exception creating SSLContext", false),
    TECHNICAL_MQ_ERROR("DAT0012", "An error occurred while trying to access to MQ", false),
    TECHNICAL_FREEMARKER_ERROR("DAT0007", "An error occurred while processing freemarker", false),
    TECHNICAL_JSON_ERROR("DAT0007", "An error occurred while processing json", false),
    SECRET_EXCEPTION("302", "An error occurred while trying to get AWS secrets", false);

    private final String code;
    private final String message;
    private final boolean retry;


}
