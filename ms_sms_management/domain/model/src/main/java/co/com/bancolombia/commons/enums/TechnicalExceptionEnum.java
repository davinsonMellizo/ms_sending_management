package co.com.bancolombia.commons.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TechnicalExceptionEnum {

    TECHNICAL_DYNAMO_EXCEPTION("T0001", "An error occurred creating a bean Dynamo",true),
    TECHNICAL_S3_EXCEPTION("T0002", "An error occurred while trying to get S3 object",true),
    INTERNAL_SERVER_ERROR("T0003", "Internal server error", true),
    GET_SECRET_NAME_DYNAMO_EXCEPTION("T0004", "An error occurred while trying to get secret name",true),
    BODY_MISSING_ERROR ("T0005", "Missing parameters per body",false),
    TECHNICAL_EXCEPTION("T0006", "An error occurred while trying to call an external service",true),
    SAVE_TOKEN_REDIS_EXCEPTION("T0007", "An error occurred save token",true),
    GET_TOKEN_REDIS_EXCEPTION("T0008", "An error occurred get token",true);


    private final String code;
    private final String message;
    private final Boolean retry;


}
