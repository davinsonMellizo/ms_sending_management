package co.com.bancolombia.commons.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BusinessErrorMessage {

    CONTACT_INVALID("150", "The contact value is invalid"),
    STATE_INVALID("150", "The state value is invalid"),
    CLIENT_ACTIVE("162", "The client is already active in alerts"),
    CLIENT_INACTIVE("166", "The client is inactive"),
    INVALID_DATA("372", "Base data is not valid"),
    CLIENT_NOT_FOUND("373", "Client Not Found"),
    CONTACT_NOT_FOUND("374", "Contact Not Found"),
    DOCUMENT_TYPE_NOT_FOUND("375", "Document Type Not Found");

    private final String code;
    private final String message;
}