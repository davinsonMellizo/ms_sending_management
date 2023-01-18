package co.com.bancolombia.commons.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum State {

    ACTIVE(1, "Activo"),
    INACTIVE(0, "Inactivo");

    private final int type;
    private final String value;

}
