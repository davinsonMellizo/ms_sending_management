package co.com.bancolombia.commons.enums;


import co.com.bancolombia.commons.exceptions.TechnicalException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TechnicalExceptionEnum {

    SEND_LOG_SQS_ERROR("DST0040", "An error occurred while sending a log "),
    TECHNICAL_RESTCLIENT_ERROR("FCT0002","An error has ocurred in the Rest Client"),
    CREATE_CLIENT_PINPOINT_ERROR("DST0040", "An error occurred while create a client pinpoint "),
    TECHNICAL_JSON_CONVERT("DST0041", "An error occurred while converting object to json "),
    SECRET_EXCEPTION("302", "An error occurred while trying to get AWS secrets"),
    INTERNAL_SERVER_ERROR("302", "Internal server error"),
    TECHNICAL_S3_EXCEPTION("DMT0006", "An error occurred while trying to get S3 object"),
    BODY_MISSING_ERROR ("301", "Missing parameters per body");

    private final String code;
    private final String message;
    public TechnicalException build(Throwable throwable) {
        return new TechnicalException(throwable, this);
    }



}
