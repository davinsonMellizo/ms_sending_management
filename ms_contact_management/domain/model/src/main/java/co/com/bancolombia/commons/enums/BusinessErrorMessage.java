package co.com.bancolombia.commons.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BusinessErrorMessage {

    CONTACT_NOT_FOUND("373", "Contact Not Found");

    private final String code;
    private final String message;
}