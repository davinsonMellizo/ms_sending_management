package co.com.bancolombia.commons.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TechnicalExceptionEnum {

    SEND_LOG_SQS_ERROR("DST0040", "An error occurred while sending a log "),
    CREATE_CLIENT_PINPOINT_ERROR("DST0040", "An error occurred while create a client pinpoint "),
    TECHNICAL_JSON_CONVERT("DST0041", "An error occurred while converting object to json "),
    SECRET_EXCEPTION("302", "An error occurred while trying to get AWS secrets"),
    TECHNICAL_S3_EXCEPTION("DMT0006", "An error occurred while trying to get S3 object");

    private final String code;
    private final String message;


}
