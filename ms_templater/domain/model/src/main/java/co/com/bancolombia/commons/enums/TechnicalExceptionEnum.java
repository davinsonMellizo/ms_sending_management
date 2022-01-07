package co.com.bancolombia.commons.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TechnicalExceptionEnum {

    TECHNICAL_SAVE("T001", "Error Creating Registry"),
    MISSING_PARAMETER("T002", "Missing Parameters");

    private final String code;
    private final String message;
}
