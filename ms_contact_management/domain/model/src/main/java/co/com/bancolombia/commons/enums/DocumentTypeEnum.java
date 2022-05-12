package co.com.bancolombia.commons.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum DocumentTypeEnum {

    CD_TYPE(0, "CD"),
    CC_TYPE(1, "CC"),
    CE_TYPE(2, "CE"),
    NIT_TYPE(3, "NIT"),
    TI_TYPE(4, "TI"),
    PAS_TYPE(5, "PAS"),
    IEPN_TYPE(6, "IEPN"),
    IEPJ_TYPE(7, "IEPJ"),
    FD_TYPE(8, "FD"),
    RC_TYPE(9, "RC");

    private int id;
    private String value;

    public static DocumentTypeEnum fromValue(String value) throws IllegalArgumentException {
        return Arrays.stream(DocumentTypeEnum.values())
                .filter(enumerator -> enumerator.value.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Tipo de documento no valido: " + value));
    }

    public static DocumentTypeEnum fromId(String value) throws IllegalArgumentException {
        return Arrays.stream(DocumentTypeEnum.values())
                .filter(enumerator -> String.valueOf(enumerator.id).equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Id de documento no valido: " + value));
    }
}
