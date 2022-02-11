package co.com.bancolombia.commons.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BusinessErrorMessage {

    BUSINESS("383", "Provider Service Not Found");

    private final String code;
    private final String message;
}