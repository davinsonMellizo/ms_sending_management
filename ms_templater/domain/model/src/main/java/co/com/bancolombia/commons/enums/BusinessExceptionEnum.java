package co.com.bancolombia.commons.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BusinessExceptionEnum {

    TEMPLATE_NOT_FOUND("B001", "Template Not Found"),
    TEMPLATE_ALREADY_EXISTS("B002", "Existing Template With Requested Parameters");

    private final String code;
    private final String message;
}
