package co.com.bancolombia.commons.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ScheduleType {
    ON_DEMAND("A demanda"),
    ONE_TIME("Una vez"),
    HOURLY("Cada hora"),
    DAILY("Diaria"),
    WEEKLY("Semanal"),
    MONTHLY("Mensual");

    private final String value;
}
