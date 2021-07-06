package co.com.bancolombia.commons.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BusinessErrorMessage {

    INVALID_DATA("372", "Base data is not valid"),
    ALERT_NOT_FOUND("373", "Alert Not Found"),
    ALERT_TRANSACTION_NOT_FOUND("374", "Alert Transaction Not Found"),
    REMITTER_NOT_FOUND("375", "Remitter Not Found"),
    PROVIDER_NOT_FOUND("376", "Provider Not Found"),
    SERVICE_NOT_FOUND("377", "Service Not Found"),
    ALERT_CLIENT_NOT_FOUND("373", "Alert Client Not Found"),
    ALERT_TEMPLATE_NOT_FOUND("373", "Alert Client Not Found");;

    private final String code;
    private final String message;
}