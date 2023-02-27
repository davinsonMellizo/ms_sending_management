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
    ALERT_HAS_NO_PUSH("372", "Alert has no push"),
    CLIENT_HAS_NO_PUSH("372", "Client has not push"),
    APPLICATION_CODE_REQUIRED("372", "Application code is required"),
    REQUIRED_REMITTER("372", "Required Remitter"),
    REQUIRED_MESSAGE("372", "Required parameter mensaje"),
    INVALID_PARAMETER("372", "Invalid Parameter"),
    CLIENT_HAS_NO_CONTACTS("016", "Client has no contacts"),
    ALERT_NOT_FOUND("120", "Alert Not Found"),
    REQUIRED_TEMPLATE("120", "Required template"),
    REQUIRED_PRIORITY("120", "Required priority"),
    INACTIVE_ALERT("120", "Inactive Alert"),
    TEMPLATE_INVALID("120", "Template for email invalid"),
    CLIENT_IDENTIFICATION_INVALID("120", "Client Identification is invalid"),
    ALERT_TRANSACTION_NOT_FOUND("374", "Alert Transaction Not Found"),
    ALERT_CLIENT_NOT_FOUND("378", "Alert Client Not Found");

    private final String code;
    private final String message;
}