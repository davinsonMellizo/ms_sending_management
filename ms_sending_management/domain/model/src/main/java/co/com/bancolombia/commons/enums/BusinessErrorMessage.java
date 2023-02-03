package co.com.bancolombia.commons.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BusinessErrorMessage {

    CLIENT_NOT_FOUND("373", "Client Not Found"),
    CLIENT_INACTIVE("373", "Inactive Client"),
    AMOUNT_NOT_EXCEEDED("373", "The client does not exceed the amount to send an alert"),
    INVALID_CONTACT("372", "Invalid Contact"),
    INVALID_REMITTER("372", "Invalid Remitter"),
    INVALID_OPERATION("372", "Invalid Operation"),
    INVALID_PARAMETER("372", "Invalid Parameter"),
    INVALID_CONTACTS("016", "Invalid Contacts"),
    ALERT_NOT_FOUND("120", "Alert Not Found"),
    MESSAGE_NOT_FOUND("120", "Message not found"),
    INACTIVE_ALERT("120", "Inactive Alert"),
    TEMPLATE_INVALID("120", "Template for email invalid"),
    CLIENT_IDENTIFICATION_INVALID("120", "Client Identification is invalid"),
    ALERT_TRANSACTION_NOT_FOUND("374", "Alert Transaction Not Found"),
    ALERT_CLIENT_NOT_FOUND("378", "Alert Client Not Found"),
    CONSUMER_NOT_FOUND("380", "Consumer Not Found"),
    PRIORITY_INVALID("381", "Consumer Not Found");

    private final String code;
    private final String message;
}