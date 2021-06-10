package co.com.bancolombia.commons.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BusinessErrorMessage {

    INVALID_DATA("372", "Base data is not valid"),
    ALERT_NOT_FOUND("373", "Alert Not Found");

    private final String code;
    private final String message;
}