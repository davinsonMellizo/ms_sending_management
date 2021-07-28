package co.com.bancolombia.commons.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BusinessErrorMessage {

    INVALID_DATA("372", "Base data is not valid"),
    CLIENT_NOT_FOUND("373", "Client Not Found"),
    CONTACT_NOT_FOUND("374", "Contact Not Found"),
    DOCUMENT_TYPE_NOT_FOUND("375", "Document Type Not Found");

    private final String code;
    private final String message;
}