package co.com.bancolombia.commons.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TechnicalExceptionEnum {

    TECHNICAL_MISSING_PARAMETERS("TE001", "Missing Parameters"),
    TECHNICAL_ERROR_REQUESTING_CAMPAIGN("TE002", "An error occurred while consulting the campaign"),
    INTERNAL_SERVER_ERROR("302", "Internal server error");

    private final String code;
    private final String message;

}
