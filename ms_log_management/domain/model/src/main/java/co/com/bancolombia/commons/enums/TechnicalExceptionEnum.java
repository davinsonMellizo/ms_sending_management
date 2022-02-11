package co.com.bancolombia.commons.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TechnicalExceptionEnum {

    SAVE_LOG_ERROR("DST0002", "An error has occurred saving the log"),
    SECRET_EXCEPTION("DST0002", "An error occurred while trying to get AWS secrets"),
    INTERNAL_SERVER_ERROR("DST0003", "Internal server error"),
    BODY_MISSING_ERROR("301", "Missing parameters per body"),
    HEADER_MISSING_ERROR("301", "Missing parameters per header");

    private final String code;
    private final String message;


}
