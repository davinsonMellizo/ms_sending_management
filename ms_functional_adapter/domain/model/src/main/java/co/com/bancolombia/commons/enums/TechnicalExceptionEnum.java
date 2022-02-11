package co.com.bancolombia.commons.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TechnicalExceptionEnum {

    TECHNICAL_CONFIG_TRANSACTION_NOT_FOUND("DAT0015", "Transaction not found in configuration"),
    TECHNICAL_JMS_ERROR("DAT0014", "An error while processing the JMS"),
    TECHNICAL_S3_EXCEPTION("DMT0006", "An error occurred while trying to get S3 object"),
    TECHNICAL_SSL_CONTEXT_ERROR("DAT0016", "Exception creating SSLContext"),
    TECHNICAL_MQ_ERROR("DAT0012", "An error occurred while trying to access to MQ"),
    TECHNICAL_FREEMARKER_ERROR("DAT0007", "An error occurred while processing freemarker"),
    TECHNICAL_JSON_ERROR("DAT0007", "An error occurred while processing json"),
    SECRET_EXCEPTION("302", "An error occurred while trying to get AWS secrets");

    private final String code;
    private final String message;


}
