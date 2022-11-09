package co.com.bancolombia.commons.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BusinessErrorMessage {

    CLIENT_NOT_FOUND("373", "Client Not Found"),
    ALERT_NOT_FOUND("120", "Alert Not Found"),
    ALERT_TRANSACTION_NOT_FOUND("374", "Alert Transaction Not Found"),
    REMITTER_NOT_FOUND("375", "Remitter Not Found"),
    PROVIDER_NOT_FOUND("376", "Provider Not Found"),
    SERVICE_NOT_FOUND("377", "Service Not Found"),
    ALERT_CLIENT_NOT_FOUND("378", "Alert Client Not Found"),
    CONSUMER_NOT_FOUND("380", "Consumer Not Found"),
    CATEGORY_NOT_FOUND("381", "Category Not Found"),
    PRIORITY_NOT_FOUND("382", "Priority Not Found"),
    PROVIDER_SERVICE_NOT_FOUND("383", "Provider Service Not Found"),
    CAMPAIGN_NOT_FOUND("384", "Campana no encontrada"),
    SCHEDULE_NOT_FOUND("384", "Horario no encontrado");

    private final String code;
    private final String message;
}