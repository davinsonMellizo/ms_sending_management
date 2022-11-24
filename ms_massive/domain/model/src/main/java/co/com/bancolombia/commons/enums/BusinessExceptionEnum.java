package co.com.bancolombia.commons.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BusinessExceptionEnum {
    BUSINESS_CAMPAIGN_NOT_FOUND("BE001", "Campana no encontrada"),
    BUSINESS_CAMPAIGN_WITHOUT_SCHEDULE_ON_DEMAND("BE002", "Campana sin horario bajo demanda");

    private final String code;
    private final String message;
}
