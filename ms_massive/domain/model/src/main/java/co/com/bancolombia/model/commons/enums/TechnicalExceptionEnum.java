package co.com.bancolombia.model.commons.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TechnicalExceptionEnum {

    TECHNICAL_MISSING_PARAMETERS("TE001", "Parametros faltantes"),
    TECHNICAL_ERROR_REQUESTING_CAMPAIGN("TE002", "Ha ocurrido un error en la consulta de campana"),
    INTERNAL_SERVER_ERROR("302", "Error interno del servidor");

    private final String code;
    private final String message;

}
