package co.com.bancolombia.commons.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BusinessErrorMessage {

    REQUIRED_MESSAGE_TEMPLATE("BS001", "Required message or template", false),
    SECRET_NAME_NOT_FOUND("BS003", "Secret name not found for priority and consumer ", false),
    TOKEN_NOT_FOUND("BS003", "An error occurred getting a token ", true),
    TEMPLATE_NOT_FOUND("BS002", "Template not found", false);

    private final String code;
    private final String message;
    private final Boolean retry;
}