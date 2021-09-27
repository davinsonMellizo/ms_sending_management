package co.com.bancolombia.model.commons.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TechnicalExceptionEnum {

    SECRET_EXCEPTION("DST0032", "An error occurred while trying to get AWS secrets"),
    INTERNAL_SERVER_ERROR("DST0033", "Internal server error"),
    BODY_MISSING_ERROR("301", "Missing parameters per body"),
    HEADER_MISSING_ERROR("301", "Missing parameters per header");

    private final String code;
    private final String message;


}
