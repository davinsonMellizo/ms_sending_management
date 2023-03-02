package co.com.bancolombia.commons.enums;


import co.com.bancolombia.commons.exceptions.TechnicalException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TechnicalExceptionEnum {

    SEND_LOG_SQS_ERROR("DST0040", "An error occurred while sending a log ",true),
    TECHNICAL_RESTCLIENT_ERROR("FCT0002","An error has ocurred in the Rest Client", true),
    TECHNICAL_EXCEPTION("006", "Communication error",true),
    CREATE_CLIENT_PINPOINT_ERROR("DST0040", "An error occurred while create a client pinpoint ", true),
    TECHNICAL_JSON_CONVERT("DST0041", "An error occurred while converting object to json ",true),
    SECRET_EXCEPTION("302", "An error occurred while trying to get AWS secrets",true),
    INTERNAL_SERVER_ERROR("302", "Internal server error", true),
    TECHNICAL_S3_EXCEPTION("DMT0006", "An error occurred while trying to get S3 object",true),
    GET_SECRET_NAME_DYNAMO_EXCEPTION("TC06", "An error occurred while trying to get secret name ",true),
    SAVE_TOKEN_REDIS_EXCEPTION("TC07", "An error occurred save token",true),
    GET_TOKEN_REDIS_EXCEPTION("TC07", "An error occurred get token",true),
    BODY_MISSING_ERROR ("301", "Missing parameters per body",true);

    private final String code;
    private final String message;
    private final Boolean retry;
    public TechnicalException build(Throwable throwable) {
        return new TechnicalException(throwable, this);
    }



}
