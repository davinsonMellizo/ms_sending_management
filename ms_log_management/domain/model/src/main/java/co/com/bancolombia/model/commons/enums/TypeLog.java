package co.com.bancolombia.model.commons.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TypeLog {
    REQ("request"),
    RES("response"),
    ERROR("errors"),
    HEAD("headers");

    private final String value;
}
