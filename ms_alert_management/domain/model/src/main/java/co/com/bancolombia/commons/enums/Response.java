package co.com.bancolombia.commons.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Response {
    SUCCESS_00("00", "Successful "),
    SUCCESS_120("120", "Successful ");

    private final String code;
    private final String description;
}
