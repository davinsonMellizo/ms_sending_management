package co.com.bancolombia.commons.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BusinessErrorMessage {

    CONTACT_INVALID("150", "The contact value is invalid", "409"),
    STATE_INVALID("151", "The state value is invalid", "409"),
    CONTACTS_EMPTY("161", "Must send a contact data value", "409"),
    CLIENT_ACTIVE("162", "Client is already registered", "409"),
    CLIENT_INACTIVE("166", "Client is not registered", "409"),
    INVALID_DATA("372", "Base data is not valid", "409"),
    CLIENT_NOT_FOUND("373", "Unregistered client", "409"),
    CONTACT_NOT_FOUND("374", "Contact Not Found", "409"),
    DOCUMENT_TYPE_NOT_FOUND("375", "Document Type Not Found", "409"),
    SUCCESS_ENROLL("120", "Successful enrollment", "409"),
    SUCCESS_UPDATE("120", "Successful update", "409"),
    SUCCESS_CHANGE("120", "Successful change", "409"),
    INVALID_PHONE("173", "Invalid phone", "409"),
    USER_NOT_VALID_SSAL_TEL("174", "Required user for branch and telephone", "409"),
    INVALID_EMAIL("175", "Invalid email", "409");

    private final String code;
    private final String message;
    private final String httpStatus;
}