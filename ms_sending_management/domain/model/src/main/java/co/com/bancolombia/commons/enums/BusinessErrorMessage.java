package co.com.bancolombia.commons.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BusinessErrorMessage {

    CLIENT_NOT_FOUND("373", "Client Not Found"),
    CLIENT_INACTIVE("373", "Inactive Client"),
    AMOUNT_NOT_EXCEEDED("373", "The client does not exceed the amount to send an alert"),
    INVALID_DATA("372", "Base data is not valid"),
    INVALID_PREFIX("372", "Invalid Prefix"),
    INVALID_CONTACT("372", "Invalid Contact"),
    INVALID_CONTACTS("372", "Invalid Contacts"),
    ALERT_NOT_FOUND("373", "Alert Not Found"),
    ALERT_TRANSACTION_NOT_FOUND("374", "Alert Transaction Not Found"),
    REMITTER_NOT_FOUND("375", "Remitter Not Found"),
    PROVIDER_NOT_FOUND("376", "Provider Not Found"),
    SERVICE_NOT_FOUND("377", "Service Not Found"),
    ALERT_CLIENT_NOT_FOUND("378", "Alert Client Not Found"),
    ALERT_TEMPLATE_NOT_FOUND("379", "Alert Template Not Found"),
    CONSUMER_NOT_FOUND("380", "Consumer Not Found"),
    CATEGORY_NOT_FOUND("381", "Category Not Found"),
    PRIORITY_NOT_FOUND("382", "Priority Not Found"),
    PROVIDER_SERVICE_NOT_FOUND("383", "Provider Service Not Found");

    private final String code;
    private final String message;
}